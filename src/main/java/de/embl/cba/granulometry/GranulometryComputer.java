package de.embl.cba.granulometry;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.Opening;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import java.util.ArrayList;

public class GranulometryComputer < T extends RealType< T > & NativeType< T > >
{
	final GranulometrySettings settings;

	public GranulometryComputer( GranulometrySettings settings )
	{
		this.settings = settings;
	}


	public GranulometryResults compute( RandomAccessibleInterval< T > rai )
	{

		if ( settings.showIntermediateResults ) ImageJFunctions.show( rai );

		HyperSphereShape openingStrel = new HyperSphereShape( settings.openingRadius );

		final RandomAccessibleInterval< T > opened = Utils.copyAsArrayImg( rai );
		Opening.openInPlace( opened, settings.interval, openingStrel, settings.numThreads );

		if ( settings.showIntermediateResults ) ImageJFunctions.show( opened );

		return null;
	}

}
