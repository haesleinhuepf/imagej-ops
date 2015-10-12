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

package net.imagej.ops.copy;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Copy;
import net.imagej.ops.Ops.Create;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.util.Intervals;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Copying {@link Img} into another {@link Img}.
 * Exists mainly for convenience reasons. 
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <T>
 */
@Plugin(type = Copy.Img.class)
public class CopyImg<T extends NativeType<T>> extends
		AbstractHybridOp<Img<T>, Img<T>> implements Copy.Img, Contingent {

	@Parameter
	private OpService ops;
	
	private ComputerOp<Iterable<T>, Iterable<T>> copyComputer;

	private FunctionOp<Img<T>, Img<T>> createFunc;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		copyComputer = (ComputerOp)ops.computer(CopyIterableInterval.class, in(), in());
		createFunc = (FunctionOp) ops().function(Create.Img.class, Img.class, 
				in());
	}
	
	@Override
	public Img<T> createOutput(final Img<T> input) {
		return createFunc.compute(input);
	}

	@Override
	public void compute(final Img<T> input, final Img<T> output) {
		copyComputer.compute(output, input);
	}

	@Override
	public boolean conforms() {
		if (out() != null) {
			return Intervals.equalDimensions(in(), out());
		}
		return true;
	}
}