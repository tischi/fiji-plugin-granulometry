package de.embl.cba.granulometry;

import ij.IJ;
import ij.gui.Plot;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.Views;

import java.util.ArrayList;

public class Utils
{
	public static void log( String msg )
	{
		IJ.log( msg );
	}

	public static < T extends NumericType< T > & NativeType< T > >
	RandomAccessibleInterval< T > copyAsArrayImg( RandomAccessibleInterval< T > rai )
	{

		RandomAccessibleInterval< T > copy = new ArrayImgFactory( rai.randomAccess().get() ).create( rai );

		// adjust origin of copy
		long[] offset = new long[ rai.numDimensions() ];
		rai.min( offset );
		copy = Views.translate( copy, offset );

		// copy
		final Cursor< T > out = Views.iterable( copy ).localizingCursor();
		final RandomAccess< T > in = rai.randomAccess();

		while( out.hasNext() )
		{
			out.fwd();
			in.setPosition( out );
			out.get().set( in.get() );
		}

		return copy;
	}

	public static void plot( ArrayList< Double > xValues , ArrayList< Double >  yValues, String xLab, String yLab )
	{
		Plot plot = new Plot("", xLab, yLab,  xValues.stream().mapToDouble( d -> d).toArray(), yValues.stream().mapToDouble( d -> d).toArray() );
		plot.show();
	}


}
