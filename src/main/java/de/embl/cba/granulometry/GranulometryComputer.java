package de.embl.cba.granulometry;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class GranulometryComputer < T extends RealType< T > & NativeType< T > >
{
	final GranulometrySettings settings;

	public GranulometryComputer( GranulometrySettings settings )
	{
		this.settings = settings;
	}


	public GranulometryResults compute( RandomAccessibleInterval< T > rai )
	{



		return null;
	}

}
