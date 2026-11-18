/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.check.util.JavaSourceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shuyang Zhou
 */
public class JavaRedundantContainsCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		String newContent = _processRaceReceivers(fileName, content);

		if (!newContent.equals(content)) {
			return newContent;
		}

		Matcher matcher = _ifStatementPattern.matcher(content);

		while (matcher.find()) {
			String methodCall = JavaSourceUtil.getMethodCall(
				content, matcher.start(2));

			String s = content.substring(
				matcher.start(2) + methodCall.length());

			if (!s.startsWith(") {")) {
				continue;
			}

			List<String> parameterList = JavaSourceUtil.getParameterList(
				methodCall);

			if (parameterList.isEmpty()) {
				continue;
			}

			String parameter = parameterList.get(0);

			if (_hasTopLevelComma(parameter)) {
				continue;
			}

			String ifStatementBody = _getIfStatementBody(
				content, matcher.start());

			if (Validator.isBlank(ifStatementBody)) {
				continue;
			}

			_checkMethodCallInIfStatementBody(
				fileName, content, ifStatementBody, matcher, parameter,
				getLineNumber(content, matcher.start()));
		}

		return content;
	}

	private void _addRaceMessage(
		String fileName, ContainsCall containsCall, String operation,
		int lineNumber) {

		String change = "the value can disappear";
		String suggestion = StringBundler.concat(
			"a single \"", operation, "\" with a null check");

		if (operation.equals("add")) {
			change = "another thread can add the element";
			suggestion = "the boolean result of a single \"add\"";
		}
		else if (operation.equals("put")) {
			change = "another thread can put the key";
			suggestion = "a single \"putIfAbsent\" or \"computeIfAbsent\"";
		}
		else if (operation.equals("remove") &&
				 !Objects.equals(containsCall.getReceiverType(), "map")) {

			change = "the element can disappear";
			suggestion = "the boolean result of a single \"remove\"";
		}

		String methodName = containsCall.getMethodName();

		String check = "the null check";

		if (methodName.startsWith("contains")) {
			check = StringBundler.concat("\"", methodName, "\"");
		}

		addMessage(
			fileName,
			StringBundler.concat(
				"Race condition on \"", containsCall.getVariableName(), "\": ",
				change, " between ", check, " and \"", operation, "\", use ",
				suggestion),
			lineNumber);
	}

	private void _checkMethodCallInIfStatementBody(
		String fileName, String content, String s, Matcher matcher,
		String parameter, int lineNumber) {

		String firstStatement = _getFirstStatement(s);

		if (firstStatement == null) {
			return;
		}

		boolean negated = false;

		if (matcher.group(1) != null) {
			negated = true;
		}

		String variableName = matcher.group(2);

		String variableTypeName = getVariableTypeName(
			content, null, content, fileName, variableName, true, false);

		if (variableTypeName == null) {
			return;
		}

		String raceReceiverType = _getRaceReceiverType(
			content, fileName, variableName);

		if (!raceReceiverType.isEmpty()) {
			return;
		}

		String operation = null;
		String suggestion = null;

		if (StringUtil.equals(matcher.group(3), "contains") &&
			variableTypeName.matches("\\w*List<.*")) {

			if (_hasOperation(
					firstStatement, variableName, "remove", parameter)) {

				operation = "remove";
				suggestion = "the boolean result of a single \"remove\"";
			}
		}
		else if (StringUtil.equals(matcher.group(3), "contains") &&
				 variableTypeName.matches("\\w*Set<.*")) {

			if (negated &&
				_hasOperation(firstStatement, variableName, "add", parameter)) {

				operation = "add";
				suggestion = "the boolean result of a single \"add\"";
			}
			else if (_hasOperation(
						firstStatement, variableName, "remove", parameter)) {

				operation = "remove";
				suggestion = "the boolean result of a single \"remove\"";
			}
		}
		else if (StringUtil.equals(matcher.group(3), "containsKey") &&
				 variableTypeName.matches("\\w*Map<.+")) {

			if (negated &&
				_hasOperation(firstStatement, variableName, "put", parameter)) {

				operation = "put";
				suggestion = "a single \"putIfAbsent\" or \"computeIfAbsent\"";
			}
			else if (_hasOperation(
						firstStatement, variableName, "get", parameter)) {

				operation = "get";
				suggestion = "a single \"get\" with a null check";
			}
			else if (_hasOperation(
						firstStatement, variableName, "remove", parameter)) {

				operation = "remove";
				suggestion = "a single \"remove\" with a null check";
			}
		}

		if ((operation == null) || (suggestion == null)) {
			return;
		}

		addMessage(
			fileName,
			StringBundler.concat(
				"Combine the \"", matcher.group(3), "\" check on \"",
				variableName, "\" and the following \"", operation, "\" into ",
				suggestion),
			lineNumber);
	}

	private String _fixFirstStatement(
		String content, ContainsCall containsCall, String operation,
		int ifStart, int bodyStart, int bodyEnd, boolean replaceIfStatement) {

		int x = _getNonwhitespacePos(content, bodyStart);

		String firstStatement = _getFirstStatement(
			content.substring(x, bodyEnd));

		String methodCall = _getBareMethodCall(
			firstStatement, containsCall, operation);

		if (methodCall == null) {
			return null;
		}

		int y = _getNonwhitespacePos(content, x + firstStatement.length());

		if (y < bodyEnd) {
			return StringBundler.concat(
				content.substring(0, containsCall.getStart()), methodCall,
				content.substring(containsCall.getEnd(), x),
				content.substring(y));
		}

		if (replaceIfStatement) {
			return StringBundler.concat(
				content.substring(0, ifStart), firstStatement,
				content.substring(bodyEnd + 1));
		}

		return null;
	}

	private String _getBareMethodCall(
		String statement, ContainsCall containsCall, String operation) {

		if (statement == null) {
			return null;
		}

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"(this\\.)?", containsCall.getVariableName(), "\\.", operation,
				"\\("));

		Matcher matcher = pattern.matcher(statement);

		if (!matcher.lookingAt()) {
			return null;
		}

		String methodCall = JavaSourceUtil.getMethodCall(statement, 0);

		if (!statement.equals(methodCall + StringPool.SEMICOLON) ||
			!Objects.equals(
				_normalize(JavaSourceUtil.getParameters(methodCall)),
				containsCall.getParameters())) {

			return null;
		}

		return methodCall;
	}

	private int _getBlockEndPos(String content, int start) {
		int x = start - 1;

		while (true) {
			x = content.indexOf(CharPool.CLOSE_CURLY_BRACE, x + 1);

			if (x == -1) {
				return content.length();
			}

			if (getLevel(content.substring(start, x + 1), "{", "}") < 0) {
				return x;
			}
		}
	}

	private int _getClosePos(
		String content, String openChar, String closeChar, int start) {

		int closePos = start;

		while (true) {
			closePos = content.indexOf(closeChar, closePos + 1);

			if (closePos == -1) {
				return -1;
			}

			String s = content.substring(start, closePos + 1);

			int level = getLevel(s, openChar, closeChar);

			if (level == 0) {
				return closePos;
			}

			if (level == -1) {
				return -1;
			}
		}
	}

	private ContainsCall _getContainsCall(
		String content, int start, int end, String fileName,
		Map<String, String> raceReceiverTypes) {

		start = _getNonwhitespacePos(content, start);

		while ((end > start) &&
			   Character.isWhitespace(content.charAt(end - 1))) {

			end--;
		}

		if ((start < end) &&
			(content.charAt(start) == CharPool.OPEN_PARENTHESIS) &&
			(_getClosePos(content, "(", ")", start) == (end - 1))) {

			start++;
			end--;
		}

		String operand = content.substring(start, end);

		Matcher matcher = _containsCallPattern.matcher(operand);

		if (!matcher.lookingAt() || (getLevel(operand) != 0)) {
			return null;
		}

		String methodCall = JavaSourceUtil.getMethodCall(
			operand, matcher.start(3));

		String methodName = matcher.group(4);

		String s = StringUtil.trim(
			operand.substring(matcher.start(3) + methodCall.length()));

		boolean presence = false;

		if (methodName.startsWith("contains")) {
			if (!s.isEmpty()) {
				return null;
			}

			if (matcher.group(1) == null) {
				presence = true;
			}
		}
		else if (matcher.group(1) != null) {
			return null;
		}
		else if (s.equals("!= null")) {
			presence = true;
		}
		else if (!s.equals("== null")) {
			return null;
		}

		String parameters = _normalize(
			JavaSourceUtil.getParameters(methodCall));

		if (parameters.isEmpty()) {
			return null;
		}

		String variableName = matcher.group(3);

		String receiverType = raceReceiverTypes.computeIfAbsent(
			variableName, key -> _getRaceReceiverType(content, fileName, key));

		if (receiverType.isEmpty()) {
			return null;
		}

		if (methodName.equals("contains")) {
			if (!receiverType.equals("list") && !receiverType.equals("set")) {
				return null;
			}
		}
		else if (methodName.equals("get")) {
			if (!receiverType.equals("map")) {
				return null;
			}
		}
		else if (!receiverType.equals("map") &&
				 !receiverType.equals("tracker")) {

			return null;
		}

		return new ContainsCall(
			end, methodName, parameters, presence, receiverType, start,
			variableName);
	}

	private String _getFirstStatement(String s) {
		int level = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if ((c == '(') || (c == '{')) {
				level++;
			}
			else if ((c == ')') || (c == '}')) {
				if ((c == '}') && (level == 0)) {
					return s.substring(0, i);
				}

				level--;
			}
			else if ((c == ';') && (level == 0)) {
				return s.substring(0, i + 1);
			}
		}

		return null;
	}

	private String _getGetterNames(String receiverType) {
		if (receiverType.equals("map")) {
			return "get|getOrDefault|getService";
		}

		if (receiverType.equals("tracker")) {
			return "getService";
		}

		return null;
	}

	private String _getIfStatementBody(String content, int pos) {
		int x = _getClosePos(content, "(", ")", pos);

		if ((x == -1) || !Objects.equals(content.substring(x, x + 3), ") {")) {
			return null;
		}

		int y = _getClosePos(content, "{", "}", x + 1);

		if (y == -1) {
			return null;
		}

		return StringUtil.trim(content.substring(x + 3, y));
	}

	private String _getLastStatement(String s) {
		int level = 0;
		int start = 0;

		for (int i = 0; i < (s.length() - 1); i++) {
			char c = s.charAt(i);

			if ((c == '(') || (c == '{')) {
				level++;
			}
			else if ((c == ')') || (c == '}')) {
				level--;

				if ((c == '}') && (level == 0)) {
					start = i + 1;
				}
			}
			else if ((c == ';') && (level == 0)) {
				start = i + 1;
			}
		}

		return StringUtil.trim(s.substring(start));
	}

	private int _getNonwhitespacePos(String content, int pos) {
		while ((pos < content.length()) &&
			   Character.isWhitespace(content.charAt(pos))) {

			pos++;
		}

		return pos;
	}

	private List<int[]> _getOperandRanges(String condition, String operator) {
		List<int[]> operandRanges = new ArrayList<>();

		int level = 0;
		int start = 0;

		for (int i = 0; i < condition.length(); i++) {
			char c = condition.charAt(i);

			if (c == '(') {
				level++;
			}
			else if (c == ')') {
				level--;
			}
			else if ((level == 0) && condition.startsWith(operator, i) &&
					 !ToolsUtil.isInsideQuotes(condition, i)) {

				operandRanges.add(new int[] {start, i});

				start = i + operator.length();
			}
		}

		operandRanges.add(new int[] {start, condition.length()});

		return operandRanges;
	}

	private String _getOperationName(String s, int pos) {
		int x = s.indexOf(CharPool.OPEN_PARENTHESIS, pos);

		return s.substring(s.lastIndexOf(CharPool.PERIOD, x) + 1, x);
	}

	private int _getOperationPos(
		String s, ContainsCall containsCall, String methodNames) {

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"(?<![\\w.])(this\\.)?(", containsCall.getVariableName(),
				")\\.(", methodNames, ")\\("));

		Matcher matcher = pattern.matcher(s);

		while (matcher.find()) {
			String parameters = JavaSourceUtil.getParameters(
				JavaSourceUtil.getMethodCall(s, matcher.start(2)));

			if (Objects.equals(matcher.group(3), "getOrDefault")) {
				List<String> parameterList = JavaSourceUtil.splitParameters(
					parameters);

				parameters = parameterList.get(0);
			}

			if (Objects.equals(
					_normalize(parameters), containsCall.getParameters())) {

				return matcher.start();
			}
		}

		return -1;
	}

	private String _getRaceReceiverType(
		String content, String fileName, String variableName) {

		String variableTypeName = getVariableTypeName(
			content, null, content, fileName, variableName, true, false);

		if (variableTypeName == null) {
			return "tracker";
		}

		if (variableTypeName.matches("(?s)(Scoped)?ServiceTrackerMap\\b.*")) {
			return "map";
		}

		if (!variableTypeName.matches(
				"(?s)(Concurrent|CopyOnWrite)\\w*\\b.*")) {

			Pattern pattern = Pattern.compile(
				StringBundler.concat(
					"[^\\w.](this\\.)?", variableName,
					"\\s*=\\s*(Collections\\s*\\.\\s*(<[^;]*?>\\s*)?",
					"synchronized|new\\s+(Concurrent|CopyOnWrite)|",
					"ConcurrentHashMap\\s*\\.\\s*(<[^;]*?>\\s*)?",
					"newKeySet\\b)"));

			Matcher matcher = pattern.matcher(content);

			if (!matcher.find()) {
				return StringPool.BLANK;
			}
		}

		String typeName = variableTypeName;

		int x = typeName.indexOf(CharPool.LESS_THAN);

		if (x != -1) {
			typeName = typeName.substring(0, x);
		}

		typeName = StringUtil.trim(typeName);

		if (typeName.endsWith("Map")) {
			return "map";
		}

		if (typeName.endsWith("Set") || typeName.endsWith("KeySetView")) {
			return "set";
		}

		return "list";
	}

	private boolean _hasMutation(String s, ContainsCall containsCall) {
		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"(?<![\\w.])(this\\.)?", containsCall.getVariableName(),
				"(\\.(add|clear|compute|merge|put|remove|replace|retain)\\w*",
				"\\(|\\s*=[^=])"));

		Matcher matcher = pattern.matcher(s);

		if (matcher.find()) {
			return true;
		}

		String parameters = containsCall.getParameters();

		if (!parameters.matches("\\w+")) {
			return false;
		}

		pattern = Pattern.compile(
			"(?<![\\w.])" + parameters + "\\s*([-+*/]?=[^=]|\\+\\+|--)");

		matcher = pattern.matcher(s);

		return matcher.find();
	}

	private boolean _hasOperation(
		String content, String variableName, String methodName,
		String parameter) {

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"\\b", variableName, "\\.\\s*", methodName, "\\("));

		Matcher matcher = pattern.matcher(content);

		if (!matcher.find()) {
			return false;
		}

		String s = content.substring(0, matcher.start());

		if (s.matches("(?s).*\\b" + variableName + "\\b.*")) {
			return false;
		}

		List<String> parameterList = JavaSourceUtil.getParameterList(
			JavaSourceUtil.getMethodCall(content, matcher.start()));

		if (parameterList.isEmpty()) {
			return false;
		}

		return Objects.equals(
			_normalize(parameterList.get(0)), _normalize(parameter));
	}

	private boolean _hasTopLevelComma(String s) {
		int level = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if ((c == '(') || (c == '<') || (c == '[')) {
				level++;
			}
			else if ((c == ')') || (c == '>') || (c == ']')) {
				level--;
			}
			else if ((c == ',') && (level == 0)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isExitStatement(String s) {
		String lastStatement = _getLastStatement(StringUtil.trim(s));

		return lastStatement.matches("(?s)(break|continue|return|throw)\\b.*");
	}

	private String _normalize(String s) {
		return s.replaceAll("\\s+", "");
	}

	private String _processRaceIfStatement(
		String fileName, String content, int ifStart, boolean elseIf,
		int conditionStart, int bodyStart, int bodyEnd,
		Map<String, String> raceReceiverTypes) {

		String condition = content.substring(conditionStart, bodyStart - 3);

		List<int[]> orOperandRanges = _getOperandRanges(condition, "||");

		int lineNumber = getLineNumber(content, ifStart);

		if (orOperandRanges.size() == 1) {
			List<int[]> andOperandRanges = _getOperandRanges(condition, "&&");

			for (int i = 0; i < andOperandRanges.size(); i++) {
				int[] andOperandRange = andOperandRanges.get(i);

				ContainsCall containsCall = _getContainsCall(
					content, conditionStart + andOperandRange[0],
					conditionStart + andOperandRange[1], fileName,
					raceReceiverTypes);

				if (containsCall == null) {
					continue;
				}

				String newContent = _processRaceIfStatementBody(
					fileName, content, containsCall, ifStart, elseIf, bodyStart,
					bodyEnd, andOperandRanges.size() == 1,
					i == (andOperandRanges.size() - 1), lineNumber);

				if (newContent != null) {
					return newContent;
				}
			}
		}

		int x = _getNonwhitespacePos(content, bodyEnd + 1);

		if (elseIf || content.startsWith("else", x) ||
			!_isExitStatement(content.substring(bodyStart, bodyEnd))) {

			return null;
		}

		String nextStatement = null;
		String s = null;

		for (int i = 0; i < orOperandRanges.size(); i++) {
			int[] orOperandRange = orOperandRanges.get(i);

			ContainsCall containsCall = _getContainsCall(
				content, conditionStart + orOperandRange[0],
				conditionStart + orOperandRange[1], fileName,
				raceReceiverTypes);

			if (containsCall == null) {
				continue;
			}

			if (s == null) {
				nextStatement = _getFirstStatement(content.substring(x));
				s = content.substring(
					bodyEnd + 1, _getBlockEndPos(content, bodyEnd + 1));
			}

			String operation = null;

			if (containsCall.isPresence()) {
				if (Objects.equals(containsCall.getReceiverType(), "set")) {
					operation = "add";
				}
			}
			else if (Objects.equals(containsCall.getMethodName(), "contains")) {
				operation = "remove";
			}
			else {
				int pos = _getOperationPos(
					s, containsCall,
					_getGetterNames(containsCall.getReceiverType()));

				if ((pos != -1) &&
					!_hasMutation(s.substring(0, pos), containsCall)) {

					_addRaceMessage(
						fileName, containsCall, _getOperationName(s, pos),
						lineNumber);
				}

				continue;
			}

			if (operation == null) {
				continue;
			}

			String methodCall = _getBareMethodCall(
				nextStatement, containsCall, operation);

			if (methodCall == null) {
				continue;
			}

			if ((i == (orOperandRanges.size() - 1)) &&
				Objects.equals(containsCall.getReceiverType(), "set")) {

				return StringBundler.concat(
					content.substring(0, containsCall.getStart()),
					StringPool.EXCLAMATION, methodCall,
					content.substring(containsCall.getEnd(), bodyEnd + 1),
					content.substring(x + nextStatement.length()));
			}

			_addRaceMessage(fileName, containsCall, operation, lineNumber);
		}

		return null;
	}

	private String _processRaceIfStatementBody(
		String fileName, String content, ContainsCall containsCall, int ifStart,
		boolean elseIf, int bodyStart, int bodyEnd, boolean singleOperand,
		boolean lastOperand, int lineNumber) {

		String body = content.substring(bodyStart, bodyEnd);
		String getterNames = _getGetterNames(containsCall.getReceiverType());

		if (containsCall.isPresence() && (getterNames != null)) {
			String condition = content.substring(
				containsCall.getEnd(), bodyStart);

			int pos = _getOperationPos(condition, containsCall, getterNames);

			if ((pos != -1) &&
				!_hasMutation(condition.substring(0, pos), containsCall)) {

				_addRaceMessage(
					fileName, containsCall, _getOperationName(condition, pos),
					lineNumber);

				return null;
			}

			pos = _getOperationPos(body, containsCall, getterNames);

			if ((pos != -1) &&
				!_hasMutation(body.substring(0, pos), containsCall)) {

				_addRaceMessage(
					fileName, containsCall, _getOperationName(body, pos),
					lineNumber);

				return null;
			}
		}

		String receiverType = containsCall.getReceiverType();

		if (receiverType.equals("tracker")) {
			return null;
		}

		String methodName = containsCall.getMethodName();
		String operation = null;

		if (containsCall.isPresence()) {
			if (methodName.startsWith("contains")) {
				operation = "remove";
			}
		}
		else if (methodName.equals("containsKey")) {
			operation = "put";
		}
		else if (receiverType.equals("set")) {
			operation = "add";
		}

		if (operation == null) {
			return null;
		}

		String firstStatement = _getFirstStatement(StringUtil.trim(body));

		if ((firstStatement == null) ||
			!_hasOperation(
				firstStatement, containsCall.getVariableName(), operation,
				containsCall.getParameters())) {

			return null;
		}

		if (lastOperand && receiverType.equals("set")) {
			int x = _getNonwhitespacePos(content, bodyEnd + 1);

			String newContent = _fixFirstStatement(
				content, containsCall, operation, ifStart, bodyStart, bodyEnd,
				singleOperand && !elseIf && !content.startsWith("else", x));

			if (newContent != null) {
				return newContent;
			}
		}

		_addRaceMessage(fileName, containsCall, operation, lineNumber);

		return null;
	}

	private String _processRaceReceivers(String fileName, String content) {
		Matcher matcher = _raceReceiverPattern.matcher(content);

		if (!matcher.find()) {
			return content;
		}

		Map<String, String> raceReceiverTypes = new HashMap<>();

		matcher = _raceIfStatementPattern.matcher(content);

		while (matcher.find()) {
			int x = _getClosePos(content, "(", ")", matcher.end() - 1);

			if ((x == -1) || !content.startsWith(") {", x)) {
				continue;
			}

			int y = _getClosePos(content, "{", "}", x + 2);

			if (y == -1) {
				continue;
			}

			String newContent = _processRaceIfStatement(
				fileName, content, matcher.start(2), matcher.group(1) != null,
				matcher.end(), x + 3, y, raceReceiverTypes);

			if (newContent != null) {
				return newContent;
			}
		}

		return content;
	}

	private static final Pattern _containsCallPattern = Pattern.compile(
		"(!)?(this\\.)?(_?[a-z]\\w*)\\.(containsKey|contains|getService|get)" +
			"\\(");
	private static final Pattern _ifStatementPattern = Pattern.compile(
		"^\t+if \\((!)?(_?[a-z]\\w+)\\.(contains(Key)?)\\(", Pattern.MULTILINE);
	private static final Pattern _raceIfStatementPattern = Pattern.compile(
		"^\t+(else )?(if) \\(", Pattern.MULTILINE);
	private static final Pattern _raceReceiverPattern = Pattern.compile(
		"Concurrent|CopyOnWrite|ServiceTrackerMap|newKeySet|" +
			"synchronized[A-Z]|\\.getService\\(");

	private static class ContainsCall {

		public int getEnd() {
			return _end;
		}

		public String getMethodName() {
			return _methodName;
		}

		public String getParameters() {
			return _parameters;
		}

		public String getReceiverType() {
			return _receiverType;
		}

		public int getStart() {
			return _start;
		}

		public String getVariableName() {
			return _variableName;
		}

		public boolean isPresence() {
			return _presence;
		}

		private ContainsCall(
			int end, String methodName, String parameters, boolean presence,
			String receiverType, int start, String variableName) {

			_end = end;
			_methodName = methodName;
			_parameters = parameters;
			_presence = presence;
			_receiverType = receiverType;
			_start = start;
			_variableName = variableName;
		}

		private final int _end;
		private final String _methodName;
		private final String _parameters;
		private final boolean _presence;
		private final String _receiverType;
		private final int _start;
		private final String _variableName;

	}

}