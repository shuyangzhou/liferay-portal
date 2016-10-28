package com.liferay.ci.thread.interruption.aspect;

public aspect Test {

	public pointcut testCall() : execution(* *(..));

}