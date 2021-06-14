package rank.pain.impl.dynamic;

import java.io.*;
import java.util.HashMap;

public class BytecodeInsertion {

    public static HashMap<InvocationData, Integer> storage = new HashMap<>();
    public static final String separator = java.io.File.separator;
    public static final String storageFileName = "storageContainer.txt";

    public static void toInsert(String source, String target, String invocationType) {
        InvocationData invocationData = new InvocationData(source, target, invocationType);
        if (!storage.containsKey(invocationData)) {
            storage.put(invocationData, 1);
        } else {
            storage.put(invocationData, storage.get(invocationData) + 1);
        }
    }

    public static void shutdown() throws DynamicAnalysisException {
        String currentFileName = System.getProperty("user.dir") + separator + storageFileName;
        FileOutputStream fileOutput;
        ObjectOutputStream objectOutput;
        try {
            fileOutput = new FileOutputStream(currentFileName);
            objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(storage);
            objectOutput.close();
        } catch (IOException e) {
            throw new DynamicAnalysisException("Error while writing data to file.");
        }
    }

}
