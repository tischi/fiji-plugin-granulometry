
import de.embl.cba.granulometry.GranulometryComputer;
import de.embl.cba.granulometry.GranulometryResults;
import de.embl.cba.granulometry.GranulometrySettings;
import de.embl.cba.granulometry.Utils;
import ij.IJ;
import ij.ImageJ;

import ij.ImagePlus;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;


import java.io.FileWriter;
import java.util.*;

/**
 * Uses Bio-Formats to extract some basic standardized
 * (format-independent) granulometry.
 */
public class GranulometryComputerTest
{

	public static < T extends RealType< T > & NativeType< T > > void main( String[] args ) throws Exception
	{

		ImageJ.main( args );

		//String imagePath = "/Users/tischer/Documents/arjen-jakobi-sachse-p62-em-quantification/data/e_267_16_B5_sec3_pos1_bodyInteriorBinaryScale0.5.zip";
		String imagePath = "/Users/tischer/Documents/fiji-plugin-granulometry/src/test/resources/granu-test.zip";
		final ImagePlus imagePlus = IJ.openImage( imagePath );

		final RandomAccessibleInterval< T > rai = ImageJFunctions.wrapReal( imagePlus );

		final GranulometrySettings granulometrySettings = new GranulometrySettings();
		granulometrySettings.openingRadiusMax = 10;
		granulometrySettings.numThreads = Runtime.getRuntime().availableProcessors();
		granulometrySettings.interval = new FinalInterval( Intervals.minAsLongArray( rai ), Intervals.maxAsLongArray( rai ) );
		granulometrySettings.calibrationUnit = imagePlus.getCalibration().getUnit();
		granulometrySettings.calibrationValue = (double) Math.round(imagePlus.getCalibration().pixelWidth * 100000d) / 100000d ;
		granulometrySettings.showIntermediateResults = false;

		if ( ( granulometrySettings.calibrationValue != imagePlus.getCalibration().pixelDepth ))
		{
			IJ.showMessage( "Sorry, currently only isotropic data is supported!" );
			return;
		}

		final GranulometryComputer granulometryComputer = new GranulometryComputer<>( granulometrySettings );
		final GranulometryResults results = granulometryComputer.compute( rai );

		for ( int i = 0; i < results.radii.size(); ++i )
		{
			results.radii.set( i, 2 * results.radii.get( i ) );
		}

		Utils.plot( results.radii, results.values, "width [" + results.spatialCalibrationUnit + "]", "" );

	}

}