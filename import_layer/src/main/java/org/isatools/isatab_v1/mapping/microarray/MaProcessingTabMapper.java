/**

 The ISAconverter, ISAvalidator & BII Management Tool are components of the ISA software suite (http://www.isa-tools.org)

 Exhibit A
 The ISAconverter, ISAvalidator & BII Management Tool are licensed under the Mozilla Public License (MPL) version
 1.1/GPL version 2.0/LGPL version 2.1

 "The contents of this file are subject to the Mozilla Public License
 Version 1.1 (the "License"). You may not use this file except in compliance with the License.
 You may obtain copies of the Licenses at http://www.mozilla.org/MPL/MPL-1.1.html.

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Original Code is the ISAconverter, ISAvalidator & BII Management Tool.

 The Initial Developer of the Original Code is the ISA Team (Eamonn Maguire, eamonnmag@gmail.com;
 Philippe Rocca-Serra, proccaserra@gmail.com; Susanna-Assunta Sansone, sa.sanson@gmail.com;
 http://www.isa-tools.org). All portions of the code written by the ISA Team are Copyright (c)
 2007-2011 ISA Team. All Rights Reserved.

 Contributor(s):
 Rocca-Serra P, Brandizi M, Maguire E, Sklyar N, Taylor C, Begley K, Field D,
 Harris S, Hide W, Hofmann O, Neumann S, Sterk P, Tong W, Sansone SA. ISA software suite:
 supporting standards-compliant experimental annotation and enabling curation at the community level.
 Bioinformatics 2010;26(18):2354-6.

 Alternatively, the contents of this file may be used under the terms of either the GNU General
 Public License Version 2 or later (the "GPL") - http://www.gnu.org/licenses/gpl-2.0.html, or
 the GNU Lesser General Public License Version 2.1 or later (the "LGPL") -
 http://www.gnu.org/licenses/lgpl-2.1.html, in which case the provisions of the GPL
 or the LGPL are applicable instead of those above. If you wish to allow use of your version
 of this file only under the terms of either the GPL or the LGPL, and not to allow others to
 use your version of this file under the terms of the MPL, indicate your decision by deleting
 the provisions above and replace them with the notice and other provisions required by the
 GPL or the LGPL. If you do not delete the provisions above, a recipient may use your version
 of this file under the terms of any one of the MPL, the GPL or the LGPL.

 Sponsors:
 The ISA Team and the ISA software suite have been funded by the EU Carcinogenomics project
 (http://www.carcinogenomics.eu), the UK BBSRC (http://www.bbsrc.ac.uk), the UK NERC-NEBC
 (http://nebc.nerc.ac.uk) and in part by the EU NuGO consortium (http://www.nugo.org/everyone).

 */

package org.isatools.isatab_v1.mapping.microarray;

import org.isatools.isatab.mapping.assay_common.ExtractTabMapper;
import org.isatools.isatab.mapping.assay_common.LabeledExtractTabMapper;
import org.isatools.isatab.mapping.microarray.HybridizationTabMapper;
import org.isatools.isatab.mapping.microarray.MaDataTransformationTabMapper;
import org.isatools.isatab.mapping.microarray.MaNormalizationTabMapper;
import org.isatools.isatab.mapping.microarray.MaRawDataTabMapper;
import org.isatools.isatab.mapping.studyFile.SampleTabMapper;
import org.isatools.tablib.mapping.pipeline.GenericProtocolApplicationTabMapper;
import org.isatools.tablib.mapping.pipeline.ProcessingsTabMapper;
import org.isatools.tablib.schema.SectionInstance;
import org.isatools.tablib.utils.BIIObjectStore;

/**
 * Maps the processing steps in a transcriptomics file.
 * <p/>
 * Jan 2008
 *
 * @author brandizi
 */
public class MaProcessingTabMapper extends ProcessingsTabMapper {

    public MaProcessingTabMapper(BIIObjectStore store, SectionInstance sectionInstance) {
        super(store, sectionInstance);

        log.info("In MaProcessingTabMapper...");
        boolean scanNameExists = checkSectionInstanceForFieldPresence(sectionInstance, "Scan Name");
        boolean dataTransformationNameExists = checkSectionInstanceForFieldPresence(sectionInstance, "Data Transformation Name");
        boolean normalizationNameExists = checkSectionInstanceForFieldPresence(sectionInstance, "Normalization Name");


        nodeMappersConfig.put("Sample Name", SampleTabMapper.class);
        nodeMappersConfig.put("Protocol REF", GenericProtocolApplicationTabMapper.class);
        nodeMappersConfig.put("Extract Name", ExtractTabMapper.class);
        nodeMappersConfig.put("Labeled Extract Name", LabeledExtractTabMapper.class);
        nodeMappersConfig.put("Hybridization Assay Name", HybridizationTabMapper.class);

        // there are a number of cases where scan name isn't really required. Having an image file is enough SlimMaRawDataTabMapper
        nodeMappersConfig.put(scanNameExists ? "Scan Name" : "Array Data File",
                scanNameExists ? MaRawDataTabMapper.class : SlimMaRawDataTabMapper.class);

        nodeMappersConfig.put(normalizationNameExists ? "Normalization Name" : "Derived Array Data File",
                normalizationNameExists ? MaNormalizationTabMapper.class : SlimMaNormalizationNameTabMapper.class);

        // there are a number of cases where the data transformation name isn't really required. Having the
        // derived data file should be enough. So we need to have slimmed down mappings. SlimMaDataTransformationTabMapper
        nodeMappersConfig.put(dataTransformationNameExists ? "Data Transformation Name" : "Derived Array Data Matrix File",
                dataTransformationNameExists ? MaDataTransformationTabMapper.class : SlimMaProcessedDataTabMapper.class);
    }

    private boolean checkSectionInstanceForFieldPresence(SectionInstance sectionInstance, String fieldName) {
        boolean fieldExists = sectionInstance.getFieldByHeader(fieldName) != null;
        log.info("Do we have a " + fieldName + "? " + fieldExists);
        return fieldExists;
    }


}
