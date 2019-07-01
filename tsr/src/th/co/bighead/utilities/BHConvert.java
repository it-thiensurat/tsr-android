package th.co.bighead.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BHConvert {

	public static <T> T from(Object src, Class<T> dstType) {
		T result = null;
		try {
			if (src != null) {
				result = dstType.newInstance();
				Class<?> srcType = src.getClass();
				Field[] fields = srcType.getFields();
				for (Field field : fields) {
					int modifiers = field.getModifiers();
					if (!(Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers))) {
						try {
							Field dstField = dstType.getField(field.getName());
							Class<?> srcDataType = field.getType();
							Class<?> dstDataType = dstField.getType();
							dstField.setAccessible(true);
							if (dstDataType.equals(srcDataType)) {
								if (dstDataType.isPrimitive()) {
									if (dstDataType.equals(int.class)) {
										dstField.setInt(result, field.getInt(src));
									} else if (dstDataType.equals(short.class)) {
										dstField.setShort(result, field.getShort(src));
									} else if (dstDataType.equals(long.class)) {
										dstField.setLong(result, field.getLong(src));
									} else if (dstDataType.equals(double.class)) {
										dstField.setDouble(result, field.getDouble(src));
									} else if (dstDataType.equals(float.class)) {
										dstField.setFloat(result, field.getFloat(src));
									} else if (dstDataType.equals(boolean.class)) {
										dstField.setBoolean(result, field.getBoolean(src));
									} else if (dstDataType.equals(byte.class)) {
										dstField.setByte(result, field.getByte(src));
									} else if (dstDataType.equals(char.class)) {
										dstField.setChar(result, field.getChar(src));
									}
								} else {
									dstField.set(result, field.get(src));
								}
							} else {

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
