
/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.arithmetic.real;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.MathOps;
import net.imagej.ops.Op;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse cosecant of
 * the real component of an input real number.
 * 
 * @author Barry DeZonia
 * @author Jonathan Hale
 */
@Plugin(type = Op.class, name = MathOps.Arccsc.NAME)
public class RealArccsc<I extends RealType<I>, O extends RealType<O>> extends
	AbstractStrictFunction<I, O> implements MathOps.Arccsc
{

	private final static RealArccos<DoubleType, DoubleType> acos =
		new RealArccos<DoubleType, DoubleType>();
	@Parameter
	private final DoubleType angle = new DoubleType();
	@Parameter
	private final DoubleType tmp = new DoubleType();

	@Override
	public O compute(final I input, final O output) {
		final double xt = input.getRealDouble();
		if ((xt > -1) && (xt < 1)) throw new IllegalArgumentException(
			"arccsc(x) : x out of range");
		else if (xt == -1) output.setReal(-Math.PI / 2);
		else if (xt == 1) output.setReal(Math.PI / 2);
		else {
			tmp.setReal(Math.sqrt(xt * xt - 1) / xt);
			acos.compute(tmp, angle);
			output.setReal(angle.getRealDouble());
		}
		return output;
	}
}