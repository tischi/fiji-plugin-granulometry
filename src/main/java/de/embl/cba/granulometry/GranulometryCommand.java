package de.embl.cba.granulometry;


import ij.IJ;
import ij.ImagePlus;
import net.imagej.DatasetService;
import net.imagej.ops.OpService;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import org.scijava.app.StatusService;
import org.scijava.command.Command;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

@Plugin(type = Command.class, menuPath = "Plugins>Morphology>Granulometry" )
public class GranulometryCommand<T extends RealType<T> & NativeType< T > > implements Command
{
	@Parameter
	public UIService uiService;

	@Parameter
	public DatasetService datasetService;

	@Parameter
	public LogService logService;

	@Parameter
	public OpService opService;

	@Parameter
	public StatusService statusService;

	@Parameter( required = false )
	public ImagePlus imagePlus;

	de.embl.cba.granulometry.settings settings = new settings();

//	public static final String FROM_DIRECTORY = "From directory";
//	public static final String CURRENT_IMAGE = "Current image";
//
//	@Parameter( choices = { FROM_DIRECTORY, CURRENT_IMAGE })
//	public String inputModality = FROM_DIRECTORY;
//
//	@Parameter
//	public String fileNameEndsWith = ".czi,.lsm";


	@Parameter
	public long openingRadiusMax = settings.openingRadiusMax;

	@Parameter
	public boolean showIntermediateResults = settings.showIntermediateResults;

	public void run()
	{
		setSettingsFromUI();

		final RandomAccessibleInterval< T > rai = ImageJFunctions.wrapReal( imagePlus );
		settings.numThreads = Runtime.getRuntime().availableProcessors();
		settings.interval = new FinalInterval( Intervals.minAsLongArray( rai ), Intervals.maxAsLongArray( rai ) );
		settings.calibrationUnit = imagePlus.getCalibration().getUnit();
		settings.calibrationValue = (double) Math.round(imagePlus.getCalibration().pixelWidth * 100000d) / 100000d ;
		settings.showIntermediateResults = false;

		if ( ( settings.calibrationValue != imagePlus.getCalibration().pixelDepth ))
		{
			IJ.showMessage( "Sorry, currently only isotropic data is supported!" );
			return;
		}

		Utils.log( "Granulometry (fraction of pixels) for image: " + imagePlus.getTitle() );

		final GranulometryComputer granulometryComputer = new GranulometryComputer<>( settings );
		final GranulometryResults results = granulometryComputer.compute( rai );

		Utils.plot( results.widths, results.values, "width [" + results.spatialCalibrationUnit + "]", "" );


	}

	public void setSettingsFromUI()
	{
		settings.showIntermediateResults = showIntermediateResults;
		settings.openingRadiusMax = openingRadiusMax;
	}


}
