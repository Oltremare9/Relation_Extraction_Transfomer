package ExtractFromTuple;

import Util.hash.Murmurs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReadFile {
    HashMap<String, String> task = new HashMap<>();
    HashMap<String, String> method = new HashMap<>();
    HashMap<String, String> material = new HashMap<>();
    HashMap<String, String> metric = new HashMap<>();
    HashMap<String, String> generic = new HashMap<>();
    HashMap<String, String> otherScientificTerm = new HashMap<>();

    HashMap<String, String> hyponym_of = new HashMap<>();
    HashMap<String, String> feature_of = new HashMap<>();
    HashMap<String, String> used_for = new HashMap<>();
    HashMap<String, String> compare = new HashMap<>();
    HashMap<String, String> conjunction = new HashMap<>();
    HashMap<String, String> coref = new HashMap<>();
    HashMap<String, String> part_of = new HashMap<>();
    HashMap<String, String> evaluate_for = new HashMap<>();

    static int duplicateEntity = 0;
    static int duplicateRelation = 0;
    static int genericNum = 0;

    public void readFile(String filePath) throws IOException {
        File fileFolder = new File(filePath);

        File[] files = fileFolder.listFiles();
        for (File file : files) {
            HashMap<String, String> currentEntityMap = new HashMap<>();
            HashMap<String, String> currentTypeMap = new HashMap<>();
            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');
            String suffix = fileName.substring(index);
            //过滤非ann文件
            if (!suffix.equals(".ann")) {
                continue;
            }
            //System.out.println(suffix);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                //T1	Generic 26 32	system
                if (str.startsWith("T")) {
                    String splitByTab[] = str.split("\t");
                    String currentId = splitByTab[0];
                    String entityName = splitByTab[2];
                    String splitByBlank[] = splitByTab[1].split(" ");
                    String type = splitByBlank[0];
                    //generic存在误差 指代自身 暂时过滤
                    if (type.equals("Generic")) {
                        genericNum++;
                        continue;
                    }
                    //节点存放入临时结果中
                    currentEntityMap.put(currentId, entityName);
                    currentTypeMap.put(currentId, type);
                    //节点存放入最终结果中
                    HashMap map = getMap(type);
                    if (!map.containsKey(entityName)) {
                        map.put(entityName, Murmurs.hashUnsigned(entityName)+type);
                    } else {
                        duplicateEntity++;
                    }

                } else {
                    String splitByTab[] = str.split("\t");
                    String splitByBlank[] = splitByTab[1].split(" ");
                    String relationName = splitByBlank[0];
                    String id1 = splitByBlank[1].split(":")[1];
                    String id2 = splitByBlank[2].split(":")[1];
                    if (currentEntityMap.containsKey(id1) && currentEntityMap.containsKey(id2)) {
                        HashMap relationMap = getMap(relationName);
                        String entity1Name = currentEntityMap.get(id1);
                        String entity2Name = currentEntityMap.get(id2);
                        Map map1 = getMap(currentTypeMap.get(id1));
                        Map map2 = getMap(currentTypeMap.get(id2));
                        String uuid1 = (String) map1.get(entity1Name);
                        String uuid2 = (String) map2.get(entity2Name);
                        if (!relationMap.containsKey(uuid1) || !relationMap.containsValue(uuid2)) {
                            relationMap.put(uuid1, uuid2);
                        } else {
                            duplicateRelation++;
                        }
                    }
                }
            }

            //break;
        }
        System.out.println(1);
    }

    public static void main(String args[]) {
        String filePath = "C:\\Users\\11346\\Desktop\\sciERC\\raw_data\\";
        ReadFile readFile = new ReadFile();
        try {
            readFile.readFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readFile.output();
    }

    public HashMap getMap(String type) {
        switch (type) {
            case "Task":
                return task;
            case "Material":
                return material;
            case "OtherScientificTerm":
                return otherScientificTerm;
            case "Generic":
                return generic;
            case "Method":
                return method;
            case "Metric":
                return metric;
            case "COREF":
                return coref;
            case "FEATURE-OF":
                return feature_of;
            case "CONJUNCTION":
                return conjunction;
            case "USED-FOR":
                return used_for;
            case "PART-OF":
                return part_of;
            case "HYPONYM-OF":
                return hyponym_of;
            case "EVALUATE-FOR":
                return evaluate_for;
            case "COMPARE":
                return compare;
            default:
                System.out.println("error");
                return null;
        }
    }

    public void output() {
        File entities = new File("entities\\");
        File relations = new File("relations\\");
        if (!entities.exists()) {
            entities.mkdirs();// 能创建多级目录
        }
        if (!relations.exists()) {
            relations.mkdirs();// 能创建多级目录
        }
        //实体写入
        CsvOperation.writeCSV(entities.getAbsolutePath() + "\\task.csv", task,1);
        CsvOperation.writeCSV(entities.getAbsolutePath() + "\\method.csv", method,1);
        CsvOperation.writeCSV(entities.getAbsolutePath() + "\\material.csv", material,1);
        CsvOperation.writeCSV(entities.getAbsolutePath() + "\\metric.csv", metric,1);
        CsvOperation.writeCSV(entities.getAbsolutePath() + "\\generic.csv", generic,1);
        CsvOperation.writeCSV(entities.getAbsolutePath() + "\\otherScientificTerm.csv", otherScientificTerm,1);

        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\hyponym_of.csv", hyponym_of,2);
        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\feature_of.csv", feature_of,2);
        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\used_for.csv", used_for,2);
        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\compare.csv", compare,2);
        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\conjunction.csv", conjunction,2);
        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\coref.csv", coref,2);
        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\part_of.csv", part_of,2);
        CsvOperation.writeCSV(relations.getAbsolutePath() + "\\evaluate_for.csv", evaluate_for,2);

    }

}
