package academia_dev;

import java.lang.reflect.Field;
import java.util.List;

public class GenericCsvExporter {

    public static String exportToCsv(List<?> items, List<String> columns) {
        if (items == null || items.isEmpty() || columns == null || columns.isEmpty()) {
            return "Nenhum dado para exportar.";
        }
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(String.join(";", columns)).append("\n");
        for (Object item : items) {
            StringBuilder rowBuilder = new StringBuilder();
            Class<?> clazz = item.getClass();
            for (int i = 0; i < columns.size(); i++) {
                String columnName = columns.get(i);
                try {
                    Field field = getFieldInHierarchy(clazz, columnName);          
                    field.setAccessible(true); 
                    Object value = field.get(item);
                    rowBuilder.append(value != null ? value.toString() : "");

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    rowBuilder.append("N/A");
                }
                if (i < columns.size() - 1) {
                    rowBuilder.append(";");
                }
            }
            csvBuilder.append(rowBuilder.toString()).append("\n");
        }

        return csvBuilder.toString();
    }

    private static Field getFieldInHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("campo " + fieldName + " não encontrado");
    }
}