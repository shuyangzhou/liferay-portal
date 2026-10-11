#!/usr/bin/env bash

function main {
	if ! git rev-parse --quiet --verify "${1}^{commit}" > /dev/null
	then
		echo "Unable to resolve the merge base \"${1}\"" >&2

		exit 1
	fi

	local skill_dir

	skill_dir=$(dirname "${0}")

	local validation_file

	for validation_file in "${skill_dir}/validations/branch"/*.md "${skill_dir}/validations/portal"/*.md
	do
		_print_validation "${1}" "${validation_file}"
	done

	local workspace

	git diff --name-only --no-renames "${1}...HEAD" | \
		sed -e "s#^workspaces/\([^/]*-workspace\)/.*#\1#p" -n | \
		sort --unique | \
		while IFS= read -r workspace
		do
			for validation_file in "${skill_dir}/validations/workspaces"/*.md
			do
				_print_validation "${1}" "${validation_file}" "${workspace}"
			done
		done
}

function _print_validation {
	local paths

	paths=$(bash "$(dirname "${0}")/select_paths.sh" "${1}" "${2}" "${3}")

	if [[ -z ${paths} ]]
	then
		echo "-- ${2} ${3} (not fired)"

		return
	fi

	echo "== ${2} ${3} ($(echo "${paths}" | wc -l | tr -d " ") paths)"

	awk '/^## / {print_section = ($0 == "## Preconditions" || $0 == "## Time Estimate")} print_section' "${2}"

	echo
}

main "${@}"