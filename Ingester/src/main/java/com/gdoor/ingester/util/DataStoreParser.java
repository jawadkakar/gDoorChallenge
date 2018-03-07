package com.gdoor.ingester.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gdoor.ingester.model.DataStore;

import java.io.File;
import java.io.IOException;

/**
 * This class parses the data store file which is given as an input to the GdoorCodeChallenge
 * when the software starts.
 *
 * taskName: GoldenHorn
 * date: 2018-3-10
 * keywords: mediterranean food, aegean sea, Greece, Athens, Michael D. Higgins, Ireland
 * active: true
 *
 */
public class DataStoreParser {

    public static DataStore dataStoreParser(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        DataStore result = mapper.readValue(file, DataStore.class);
        return result;
    }

}
