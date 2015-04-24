package com.liferay.cobertura.coveragedata;

import net.sourceforge.cobertura.coveragedata.ClassData;
public class PackageData extends CoverageDataContainer {

	public PackageData(String name) {
		this.name = name;
	}

	public ClassData addClassData(ClassData classData) {
		ClassData previousClassData = (ClassData)children.putIfAbsent(
			classData.getBaseName(), classData);

		if (previousClassData != null) {
			classData = previousClassData;
		}

		return classData;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)return true;

		if ((obj == null) || !(obj.getClass().equals(this.getClass())))
			return false;

		PackageData packageData = (PackageData)obj;
			return super.equals(obj) && this.name.equals(packageData.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	private static final long serialVersionUID = 7;

	private final String name;

}