
import de.embl.cba.granulometry.GranulometryComputer;
import de.embl.cba.granulometry.GranulometryResults;
import de.embl.cba.granulometry.GranulometrySettings;
import ij.IJ;
import ij.ImageJ;

import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
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

	public static < T extends RealType< T > & NativeType< T > > void main( String[] args ) throws Exception {

		ImageJ.main( args );

		String imagePath = "/Volumes/cba/exchange/OeyvindOedegaard/yaml_project/20180627_LSM780M2_208_ibidi1_fcs_B_Posx96.lsm";
		final ImagePlus imagePlus = IJ.openImage( imagePath );
		final RandomAccessibleInterval< T > rai = ImageJFunctions.wrapReal( imagePlus );

		final GranulometrySettings granulometrySettings = new GranulometrySettings();
		final GranulometryComputer< T > granulometryComputer = new GranulometryComputer<>( granulometrySettings );
		final GranulometryResults results = granulometryComputer.compute( rai );


	}

}