import de.embl.cba.granulometry.GranulometryCommand;
import ij.IJ;
import ij.ImagePlus;
import net.imagej.ImageJ;

public class GranulometryCommandTest
{
	public static void main(final String... args) throws Exception
	{
		final ImageJ ij = new ImageJ();
		ij.ui().showUI();


		final ImagePlus imagePlus = IJ.openImage( GranulometryCommandTest.class.getResource( "granu-test.zip" ).getPath() );
		imagePlus.show();

		// invoke the plugin
		ij.command().run( GranulometryCommand.class, true );
	}

}
