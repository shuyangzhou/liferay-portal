package com.liferay.cobertura.coveragedata;

import net.sourceforge.cobertura.coveragedata.ClassData;

public class PackageData extends CoverageDataContainer
		implements Comparable
{

	private static final long serialVersionUID = 7;

	private String name;

	public PackageData(String name)
	{
		if (name == null)
			throw new IllegalArgumentException(
					"Package name must be specified.");
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

	public int compareTo(Object o)
	{
		if (!o.getClass().equals(PackageData.class))
			return Integer.MAX_VALUE;
		return this.name.compareTo(((PackageData)o).name);
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if ((obj == null) || !(obj.getClass().equals(this.getClass())))
			return false;

		PackageData packageData = (PackageData)obj;
			return super.equals(obj) && this.name.equals(packageData.name);
	}

	public int hashCode()
	{
		return this.name.hashCode();
	}

}
