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
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PackageData)) {
			return false;
		}

		PackageData packageData = (PackageData)obj;

		if (name.equals(packageData.name) && super.equals(packageData)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	private static final long serialVersionUID = 7;

	private final String name;

}