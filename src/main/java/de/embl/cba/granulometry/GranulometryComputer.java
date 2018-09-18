package de.embl.cba.granulometry;

import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.morphology.Opening;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

public class GranulometryComputer < T extends RealType< T > & NativeType< T > >
{
	final de.embl.cba.granulometry.settings settings;

	public GranulometryComputer( de.embl.cba.granulometry.settings settings )
	{
		this.settings = settings;
	}


	public GranulometryResults compute( RandomAccessibleInterval< T > rai )
	{

		// TODO: Evaluation interval (possibly only central plane for the data from Carsten).

		if ( settings.showIntermediateResults ) ImageJFunctions.show( rai, "input data" );

		final GranulometryResults results = new GranulometryResults();
		results.spatialCalibrationUnit = settings.calibrationUnit;

		final RandomAccessibleInterval< T > opened = Utils.copyAsArrayImg( rai );

		final double totalSum = computeSum( rai );

		for ( long openingRadius = settings.openingRadiusMin; openingRadius <= settings.openingRadiusMax; openingRadius += settings.openingRadiusDelta )
		{
			final HyperSphereShape openingStrel = new HyperSphereShape( openingRadius );
			final RandomAccessibleInterval< T > previousOpened = Utils.copyAsArrayImg( opened );
			Opening.openInPlace( opened, settings.interval, openingStrel, settings.numThreads );

			final RandomAccessibleInterval< T > difference = createDifference( previousOpened, opened );
			double sum = computeSum( difference );

			results.radii.add( openingRadius * settings.calibrationValue );
			results.widths.add( 2 * openingRadius * settings.calibrationValue );
			results.values.add( sum / totalSum );

			logResult( totalSum, openingRadius, sum );

			if ( settings.showIntermediateResults ) ImageJFunctions.show( difference, "radius " + openingRadius );
		}

		return results;
	}

	private void logResult( double totalSum, long openingRadius, double sum )
	{
		Utils.log( "Radius [" + settings.calibrationUnit + "] " +
				String.format( " %.3f", openingRadius * settings.calibrationValue )
				+ ": " + String.format( " %.3f", sum / totalSum ) );
	}

	private double computeSum( RandomAccessibleInterval< T > difference )
	{
		double sum = 0;
		final Cursor< T > cursor = Views.iterable( difference ).cursor();
		while( cursor.hasNext() )
		{
			sum += cursor.next().getRealDouble();
		}
		return sum;
	}

	private RandomAccessibleInterval< T > createDifference( RandomAccessibleInterval< T > imgA, RandomAccessibleInterval< T > imgB )
	{
		final RandomAccessibleInterval< T > result = Utils.copyAsArrayImg( imgA );

		LoopBuilder.setImages( result, imgA, imgB ).forEachPixel( ( r, a, b ) -> r.setReal( a.getRealDouble() - b.getRealDouble() ) );

		return result;

	}

}
