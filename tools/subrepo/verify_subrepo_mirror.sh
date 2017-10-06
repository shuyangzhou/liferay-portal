#!/bin/bash
#
# Author: Samuel Trong Tran <samuel.tran@liferay.com>
# Platform: Linux/Unix
#

set -o pipefail

error() {
	MESSAGE=$1

	warn "error: ${MESSAGE}"

	exit 1
}

help() {
	info "Usage: ./verify_subrepo_mirror.sh [-a] [-p PATTERN] [SUBREPO_NAME]"
	info " -a: All subrepos. Must omit -p and SUBREPO_NAME."
	info " -p: Verify subrepos matching a regex pattern. Must omit -a and SUBREPO_NAME."
}

info() {
	MESSAGE=$1

	echo "${MESSAGE}"
}

warn() {
	MESSAGE=$1

	echo "${MESSAGE}" >&2
}

OPTION_ALL=
PATTERN=

while getopts "ap:" OPT
do
	case ${OPT} in
	a)
		OPTION_ALL=true
		;;
	p)
		PATTERN="${OPTARG}"
		;;
	*)
		help

		exit 1
		;;
	esac
done

shift $((OPTIND-1))

SUBREPO_NAME=$1

if [[ "$2" ]]
then
	help

	exit 1
fi

if [[ -z "${PATTERN}" ]] && [[ -z "${OPTION_ALL}" ]] && [[ -z "${SUBREPO_NAME}" ]]
then
	help

	exit 1
elif [[ "${SUBREPO_NAME}" ]] && [[ "${PATTERN}" || "${OPTION_ALL}" ]]
then
	error "Bad usage. Specifiying SUBREPO_NAME cannot be used together with -a or -p."
elif [[ "${PATTERN}" ]] && [[ "${OPTION_ALL}" ]]
then
	error "Bad usage. The -a option cannot be combined with -p."
fi

#
# Verify environment.
#

if [[ "${PWD}" != *"/tools/subrepo" ]]
then
	error "This script can only be run from within the tools/subrepo directory."
fi

if [[ "${PWD}" != *"liferay-portal/tools/subrepo" ]]
then
	error "This script can only be run from within the liferay-portal repository."
fi

if [[ ! -e ../../../liferay-portal-ee ]]
then
	error "liferay-portal-ee does not exist at ../../../liferay-portal-ee"
fi

if [[ "$(uname)" == "Darwin" ]]
then
	MD5=md5
else
	MD5=md5sum
fi

PUBLIC_BRANCHES=(
	"7.0.x"
	"master"
)

if [[ "${SUBREPO_NAME}" ]]
then
	SUBREPOS=()

	for PUBLIC_BRANCH in "${PUBLIC_BRANCHES[@]}"
	do
		SUBREPO_SEARCH="$(git -C ../.. grep "/${SUBREPO_NAME}.git" "refs/remotes/upstream/${PUBLIC_BRANCH}" -- '*.gitrepo' | grep ":modules/apps/" | tr '\t' ' ' | sed 's@.*/@@' | sed 's/\.git.*//')"

		if [[ -z "${SUBREPO_SEARCH}" ]] || [[ "$(echo "${SUBREPOS[@]}" | grep "^${SUBREPO_SEARCH}\$")" ]]
		then
			continue
		fi

		SUBREPOS=("${SUBREPOS[@]}" "${SUBREPO_SEARCH}")
	done

	if [[ -z "$(echo "${SUBREPOS[@]}" | grep '[a-zA-Z]')" ]]
	then
		error "Failed to find a valid .gitrepo file for ${SUBREPO_NAME}."
	fi
else
	ALL_SUBREPOS=()

	for PUBLIC_BRANCH in "${PUBLIC_BRANCHES[@]}"
	do
		SUBREPO_SEARCH="$(git -C ../.. grep "git@github.com" "refs/remotes/upstream/${PUBLIC_BRANCH}" -- '*.gitrepo' | grep ":modules/apps/" | tr '\t' ' ' | sed 's@.*/@@' | sed 's/\.git.*//')"

		if [[ -z "${SUBREPO_SEARCH}" ]] || [[ "$(echo "${ALL_SUBREPOS[@]}" | grep "^${SUBREPO_SEARCH}\$")" ]]
		then
			continue
		fi

		ALL_SUBREPOS=("${ALL_SUBREPOS[@]}" "${SUBREPO_SEARCH}")
	done

	SUBREPOS=($(printf '%s\n' "${ALL_SUBREPOS[@]}" | grep "${PATTERN}"))

	if [[ -z "$(echo "${SUBREPOS[@]}" | grep '[a-zA-Z]')" ]]
	then
		if [[ "${PATTERN}" ]]
		then
			error "No subrepos found matching the specified filter."
		else
			error "No subrepos found."
		fi
	fi
fi

info "Verifying ${#SUBREPOS[@]} subrepos..."
info

for SUBREPO in "${SUBREPOS[@]}"
do
	OUTPUT="$(curl -H "Authorization: token ${GITHUB_API_TOKEN}" -L -s "https://api.github.com/repos/liferay/${SUBREPO}/branches" -X GET 2>&1)"

	if [[ -z "$(echo "${OUTPUT}" | grep '\[')" ]] || [[ -z "$(echo "${OUTPUT}" | grep '\]')" ]]
	then
		warn "Failed to get branches at liferay/${SUBREPO}."

		warn "${OUTPUT}"

		continue
	fi

	PUBLIC_BRANCH_JSON="$(echo "${OUTPUT}" | tr '\n' ' ' | sed 's/ //g' | sed 's/"name"/\'$'\n&/g' | grep '"name"')"

	OUTPUT="$(curl -H "Authorization: token ${GITHUB_API_TOKEN}" -L -s "https://api.github.com/repos/liferay/${SUBREPO}/branches" -X GET 2>&1)"

	if [[ -z "$(echo "${OUTPUT}" | grep '\[')" ]] || [[ -z "$(echo "${OUTPUT}" | grep '\]')" ]]
	then
		warn "Failed to get branches at liferay/${SUBREPO}-private."

		warn "${OUTPUT}"

		continue
	fi

	PRIVATE_BRANCH_JSON="$(echo "${OUTPUT}" | tr '\n' ' ' | sed 's/ //g' | sed 's/"name"/\'$'\n&/g' | grep '"name"')"

	for PUBLIC_BRANCH in "${PUBLIC_BRANCHES[@]}"
	do
		PUBLIC_SHA="$(echo "${PUBLIC_BRANCH_JSON}" | grep "\"name\":\"${PUBLIC_BRANCH}\"" | sed 's/.*"sha":"//' | sed 's/".*//')"

		if [[ -z "${PUBLIC_SHA}" ]]
		then
			continue
		fi

		PRIVATE_SHA="$(echo "${PRIVATE_BRANCH_JSON}" | grep "\"name\":\"${PUBLIC_BRANCH}\"" | sed 's/.*"sha":"//' | sed 's/".*//')"

		if [[ "${PUBLIC_SHA}" != "${PRIVATE_SHA}" ]]
		then
			warn "Branch ${PUBLIC_BRANCH} is out-of-sync at ${SUBREPO}-private"
		fi
	done
done