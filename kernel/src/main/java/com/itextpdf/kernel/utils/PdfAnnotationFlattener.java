/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    This program is offered under a commercial and under the AGPL license.
    For commercial licensing, contact us at https://itextpdf.com/sales.  For AGPL licensing, see below.

    AGPL licensing:
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itextpdf.kernel.utils;

import com.itextpdf.commons.utils.MessageFormatUtil;
import com.itextpdf.kernel.exceptions.KernelExceptionMessageConstant;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.utils.annotationsflattening.IAnnotationFlattener;
import com.itextpdf.kernel.utils.annotationsflattening.PdfAnnotationFlattenFactory;

import java.util.List;

/**
 * Utility class for flattening annotations.
 * <p>
 * This class can be used to remove interactive elements from a PDF document.
 */
public class PdfAnnotationFlattener {

    private final PdfAnnotationFlattenFactory pdfAnnotationFlattenFactory;

    /**
     * Creates a new instance of {@link PdfAnnotationFlattener}.
     *
     * @param pdfAnnotationFlattenFactory the factory for creating annotation flatten workers
     */
    public PdfAnnotationFlattener(PdfAnnotationFlattenFactory pdfAnnotationFlattenFactory) {
        this.pdfAnnotationFlattenFactory = pdfAnnotationFlattenFactory;
    }

    /**
     * Creates a new instance of {@link PdfAnnotationFlattener}.
     * The default factory will be used for creating annotation flatten workers.
     */
    public PdfAnnotationFlattener() {
        this.pdfAnnotationFlattenFactory = new PdfAnnotationFlattenFactory();
    }

    /**
     * Flattens the annotations on the page according to the defined implementation of
     * {@link IAnnotationFlattener}.
     *
     * @param annotationsToFlatten the annotations that should be flattened.
     * @param page                 the page where the annotations are located.
     */
    public void flatten(List<PdfAnnotation> annotationsToFlatten, PdfPage page) {
        if (page == null) {
            throw new PdfException(
                    MessageFormatUtil.format(KernelExceptionMessageConstant.ARG_SHOULD_NOT_BE_NULL, "page"));
        }
        if (annotationsToFlatten == null) {
            throw new PdfException(
                    MessageFormatUtil.format(KernelExceptionMessageConstant.ARG_SHOULD_NOT_BE_NULL,
                            "annotationsToFlatten"));
        }
        for (final PdfAnnotation pdfAnnotation : annotationsToFlatten) {
            final IAnnotationFlattener worker = pdfAnnotationFlattenFactory.getAnnotationFlattenWorker(
                    pdfAnnotation.getSubtype());
            worker.flatten(pdfAnnotation, page);
        }
    }

    /**
     * Flattens all annotations on the page according to the defined implementation of
     * {@link IAnnotationFlattener}.
     *
     * @param page the page where the annotations are located.
     */
    public void flatten(PdfPage page) {
        if (page == null) {
            throw new PdfException(
                    MessageFormatUtil.format(KernelExceptionMessageConstant.ARG_SHOULD_NOT_BE_NULL, "page"));
        }
        flatten(page.getAnnotations(), page);
    }
}
