package com.liferay.cobertura.coveragedata;

import net.sourceforge.cobertura.coveragedata.ClassData;

public class PackageData extends CoverageDataContainer
{

	private static final long serialVersionUID = 7;

	private String name;

	public PackageData(String name)
	{
		this.name = name;
	}

	public void addClassData(ClassData classData)
	{
			if (children.containsKey(classData.getBaseName()))
				throw new IllegalArgumentException("Package " + this.name
						+ " already contains a class with the name "
						+ classData.getBaseName());

			children.put(classData.getBaseName(), classData);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if ((obj == null) || !(obj.getClass().equals(this.getClass())))
			return false;

		PackageData packageData = (PackageData)obj;
			return super.equals(obj) && this.name.equals(packageData.name);
	}

	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}

}
