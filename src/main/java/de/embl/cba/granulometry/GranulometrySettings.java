package de.embl.cba.granulometry;

import net.imglib2.FinalInterval;

public class GranulometrySettings
{
	public long openingRadiusMin = 1;
	public long openingRadiusMax = 10;
	public long openingRadiusDelta = 1;

	public double calibrationValue;
	public String calibrationUnit = "nm";

	public int numThreads = 1;
	public FinalInterval interval;
	public boolean showIntermediateResults = true;

}
