/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.workflow.kaleo.definition.exception;

import com.liferay.portal.kernel.workflow.WorkflowException;

/**
 * @author Inácio Nery
 */
public class KaleoDefinitionValidationException extends WorkflowException {

	public KaleoDefinitionValidationException() {
	}

	public KaleoDefinitionValidationException(String msg) {
		super(msg);
	}

	public KaleoDefinitionValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public KaleoDefinitionValidationException(Throwable cause) {
		super(cause);
	}

	public static class MultipleInitialStateNodes
		extends KaleoDefinitionValidationException {

		public MultipleInitialStateNodes(String state1, String state2) {
			super(
				String.format(
					"Multiple initial state nodes %s and %s", state1, state2));

			_state1 = state1;
			_state2 = state2;
		}

		public String getState1() {
			return _state1;
		}

		public String getState2() {
			return _state2;
		}

		private final String _state1;
		private final String _state2;

	}

	public static class MustNotSetIncomingTransition
		extends KaleoDefinitionValidationException {

		public MustNotSetIncomingTransition(String node) {
			super(
				String.format(
					"An incoming transition was found for the %s node", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustPairedForkAndJoinNodes
		extends KaleoDefinitionValidationException {

		public MustPairedForkAndJoinNodes(String fork, String node) {
			super(
				String.format(
					"Fork %s and join %s nodes are not paired", fork, node));

			_fork = fork;
			_node = node;
		}

		public String getFork() {
			return _fork;
		}

		public String getNode() {
			return _node;
		}

		private final String _fork;
		private final String _node;

	}

	public static class MustSetAssignments
		extends KaleoDefinitionValidationException {

		public MustSetAssignments(String task) {
			super(String.format("No assignments for the %s task node", task));

			_task = task;
		}

		public String getTask() {
			return _task;
		}

		private final String _task;

	}

	public static class MustSetIncomingTransition
		extends KaleoDefinitionValidationException {

		public MustSetIncomingTransition(String node) {
			super(
				String.format(
					"No incoming transition found for the %s node", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetInitialStateNode
		extends KaleoDefinitionValidationException {

		public MustSetInitialStateNode() {
			super("No initial state node defined");
		}

	}

	public static class MustSetJoinNode
		extends KaleoDefinitionValidationException {

		public MustSetJoinNode(String fork) {
			super(
				String.format(
					"No matching join node found for the %s fork node", fork));

			_fork = fork;
		}

		public String getFork() {
			return _fork;
		}

		private final String _fork;

	}

	public static class MustSetMultipleOutgoingTransition
		extends KaleoDefinitionValidationException {

		public MustSetMultipleOutgoingTransition(String node) {
			super(
				String.format(
					"Less than 2 outgoing transitions found for the %s node",
					node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetOutgoingTransition
		extends KaleoDefinitionValidationException {

		public MustSetOutgoingTransition(String node) {
			super(
				String.format(
					"No outgoing transition found for the %s node", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetSourceNode
		extends KaleoDefinitionValidationException {

		public MustSetSourceNode(String node) {
			super(
				String.format("Unable to find source node for %s node", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetTargetNode
		extends KaleoDefinitionValidationException {

		public MustSetTargetNode(String node) {
			super(
				String.format("Unable to find target node for %s node", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetTaskFormDefinitionOrReference
		extends KaleoDefinitionValidationException {

		public MustSetTaskFormDefinitionOrReference(
			String task, String taskForm) {

			super(
				String.format(
					"Task form must specify the form reference or form " +
						"definition for task %s and form %s",
					task, taskForm));

			_task = task;
			_taskForm = taskForm;
		}

		public String getTask() {
			return _task;
		}

		public String getTaskForm() {
			return _taskForm;
		}

		private final String _task;
		private final String _taskForm;

	}

	public static class MustSetTerminalStateNode
		extends KaleoDefinitionValidationException {

		public MustSetTerminalStateNode() {
			super("No terminal state node defined");
		}

	}

	public static class UnbalancedForkAndJoinNode
		extends KaleoDefinitionValidationException {

		public UnbalancedForkAndJoinNode(String fork, String join) {
			super(
				String.format(
					"There are errors between the fork node %s and the join " +
						"node %s",
					fork, join));

			_fork = fork;
			_join = join;
		}

		public String getFork() {
			return _fork;
		}

		public String getJoin() {
			return _join;
		}

		private final String _fork;
		private final String _join;

	}

	public static class UnbalancedForkAndJoinNodes
		extends KaleoDefinitionValidationException {

		public UnbalancedForkAndJoinNodes() {
			super("There are unbalanced fork and join nodes");
		}

	}

}