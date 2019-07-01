package th.co.bighead.utilities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Annop on 22/7/2557.
 */
public abstract class BHParcelable implements Parcelable {

	public BHParcelable() {
		// TODO Auto-generated constructor stub

	}

	public BHParcelable(Parcel in) {
		String className = in.readString();
		Log.i("BHParcelable", "Constructor: " + this.getClass().getSimpleName() + "; In parcel: " + className);
		try {
			rehydrate(this, in);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final Creator<BHParcelable> CREATOR = new Creator<BHParcelable>() {
		@Override
		public BHParcelable createFromParcel(Parcel in) {
			// get class from first parcelled item
			Class<?> parceledClass;
			try {
				parceledClass = Class.forName(in.readString());
				Log.i("BHParcelable", "Creator: " + parceledClass.getSimpleName());
				// create instance of that class
				BHParcelable model = (BHParcelable) parceledClass.newInstance();
				rehydrate(model, in);
				return model;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public BHParcelable[] newArray(int size) {
			return new BHParcelable[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getClass().getName());
		try {
			dehydrate(this, dest);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return toJSON(this);
	}

	// writes fields of a BHParcelable to a parcel
	// does not include the first parcelled item -- the class name
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void dehydrate(BHParcelable model, Parcel out) throws IllegalArgumentException, IllegalAccessException {
		Log.i("BHParcelable", "dehydrating... " + model.getClass().toString());
		// get the fields
		Field[] fields = model.getClass().getDeclaredFields();
		// sort the fields so it is in deterministic order
		Arrays.sort(fields, compareMemberByName);
		// populate the fields
		for (Field field : fields) {
			field.setAccessible(true);

			Class<?> type = field.getType();
			if (type.isArray()) { // Array type;
				type = type.getComponentType();

				if (type.equals(int.class)) {
					out.writeIntArray((int[]) field.get(model));
				} else if (type.equals(long.class)) {
					out.writeLongArray((long[]) field.get(model));
				} else if (type.equals(double.class)) {
					out.writeDoubleArray((double[]) field.get(model));
				} else if (type.equals(float.class)) {
					out.writeFloatArray((float[]) field.get(model));
				} else if (type.equals(boolean.class)) {
					out.writeBooleanArray((boolean[]) field.get(model));
				} else if (type.equals(Integer.class)) {
					out.writeArray((Integer[]) field.get(model));
				} else if (type.equals(Long.class)) {
					out.writeArray((Long[]) field.get(model));
				} else if (type.equals(Double.class)) {
					out.writeArray((Double[]) field.get(model));
				} else if (type.equals(Float.class)) {
					out.writeArray((Float[]) field.get(model));
				} else if (type.equals(Boolean.class)) {
					out.writeArray((Boolean[]) field.get(model));
				} else if (type.equals(String.class)) {
					out.writeStringArray((String[]) field.get(model));
				} else if (type.equals(Date.class)) {
					Date[] dates = (Date[]) field.get(model);
					long[] dd = new long[dates.length];
					for (int ii = 0; ii < dates.length; ii++) {
						dd[ii] = dates[ii] != null ? dates[ii].getTime() : -1;
					}

					out.writeLongArray(dd);
				} else if (BHParcelable.class.isAssignableFrom(type)) {
					out.writeParcelableArray((BHParcelable[]) field.get(model), 0);
					// } else if (Collection.class.isAssignableFrom(type)) {
					// out.writeTypedList((List)field.get(model));
				} else if (type.isEnum()) {
					Enum[] values = (Enum[]) field.get(model);
					String[] dd = new String[values.length];
					for (int ii = 0; ii < values.length; ii++) {
						dd[ii] = values[ii].name();
					}

					out.writeStringArray(dd);
				} else {
					// wtf
					Log.e("BHParcelable", "Could not write field to parcel: " + " (" + type.toString() + ")");
				}
			} else if (Collection.class.isAssignableFrom(type)) {
				type = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
				List list = (List) field.get(model);
				if (type.equals(Integer.class)) {
					out.writeList(list);
				} else if (type.equals(Long.class)) {
					out.writeList(list);
				} else if (type.equals(Double.class)) {
					out.writeList(list);
				} else if (type.equals(Float.class)) {
					out.writeList(list);
				} else if (type.equals(Boolean.class)) {
					out.writeList(list);
				} else if (type.equals(String.class)) {
					out.writeStringList(list);
				} else if (type.equals(Date.class)) {
					List<Date> dates = list;
					long[] dd = new long[dates.size()];
					for (int ii = 0; ii < dates.size(); ii++) {
						dd[ii] = dates.get(ii) != null ? dates.get(ii).getTime() : -1;
					}

					out.writeLongArray(dd);
				} else if (type.isEnum()) {
					List<Enum> values = list;
					String[] dd = new String[values.size()];
					for (int ii = 0; ii < values.size(); ii++) {
						dd[ii] = values.get(ii).name();
					}

					out.writeStringArray(dd);
				} else if (BHParcelable.class.isAssignableFrom(type)) {
					out.writeTypedList(list);
				} else {
					// out.writeList(list);
					// wtf
					Log.e("BHParcelable", "Could not write field to parcel: " + " (" + type.toString() + ")");
				}
			} else {
				if (type.equals(int.class)) {
					out.writeInt(field.getInt(model));
				} else if (type.equals(long.class)) {
					out.writeLong(field.getLong(model));
				} else if (type.equals(double.class)) {
					out.writeDouble(field.getDouble(model));
				} else if (type.equals(float.class)) {
					out.writeFloat(field.getFloat(model));
				} else if (type.equals(boolean.class)) {
					out.writeInt(field.getBoolean(model) ? 1 : 0);
				} else if (type.equals(Integer.class)) {
					out.writeValue(field.get(model));
				} else if (type.equals(Long.class)) {
					out.writeValue(field.get(model));
				} else if (type.equals(Double.class)) {
					out.writeValue(field.get(model));
				} else if (type.equals(Float.class)) {
					out.writeValue(field.get(model));
				} else if (type.equals(Boolean.class)) {
					out.writeValue(field.get(model));
				} else if (type.equals(String.class)) {
					out.writeString((String) field.get(model));
				} else if (type.equals(Date.class)) {
					Date date = (Date) field.get(model);
					if (date != null) {
						out.writeLong(date.getTime());
					} else {
						out.writeLong(-1);
					}
				} else if (BHParcelable.class.isAssignableFrom(type)) {
					// why did this happen?
					// Log.e("BHParcelable", "BHParcelable F*ck up: " + " (" +
					// type.toString() + ")");
					out.writeParcelable((BHParcelable) field.get(model), 0);
					// } else if (BHParcelable[].class.isAssignableFrom(type)) {
					// out.writeParcelableArray((BHParcelable[])
					// field.get(model), 0);
					// } else if (Collection.class.isAssignableFrom(type)) {
					// out.writeTypedList((List)field.get(model));
				} else if (type.isEnum()) {
					out.writeString(((Enum) field.get(model)).name());
				} else {
					// out.writeValue(field.get(model));
					// wtf
					Log.e("BHParcelable", "Could not write field to parcel: " + " (" + type.toString() + ")");
				}
			}

		}
	}

	// reads the parcelled items and put them into this object's fields
	// must be run after getting the first parcelled item -- the class name
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static void rehydrate(BHParcelable model, Parcel in) throws IllegalArgumentException, IllegalAccessException {
		Log.i("BHParcelable", "rehydrating... " + model.getClass().toString());
		// get the fields
		Field[] fields = model.getClass().getDeclaredFields();
		// sort the fields so it is in deterministic order
		Arrays.sort(fields, compareMemberByName);
		// populate the fields
		for (Field field : fields) {
			field.setAccessible(true);
			Class<?> type = field.getType();
			if (type.isArray()) {
				type = type.getComponentType();
				int length = in.readInt();

				if (length >= 0) {
					Object array = Array.newInstance(type, length);

					if (type.equals(int.class)) {
						for (int ii = 0; ii < length; ii++) {
							Array.setInt(array, ii, in.readInt());
						}
					} else if (type.equals(long.class)) {
						for (int ii = 0; ii < length; ii++) {
							Array.setLong(array, ii, in.readLong());
						}
					} else if (type.equals(double.class)) {
						for (int ii = 0; ii < length; ii++) {
							Array.setDouble(array, ii, in.readDouble());
						}
					} else if (type.equals(float.class)) {
						for (int ii = 0; ii < length; ii++) {
							Array.setFloat(array, ii, in.readFloat());
						}
					} else if (type.equals(boolean.class)) {
						for (int ii = 0; ii < length; ii++) {
							Array.setBoolean(array, ii, in.readInt() != 0);
						}
					} else if (type.equals(Integer.class)) {
						array = in.readArray(Integer.class.getClassLoader());
					} else if (type.equals(Long.class)) {
						array = in.readArray(Long.class.getClassLoader());
					} else if (type.equals(Double.class)) {
						array = in.readArray(Double.class.getClassLoader());
					} else if (type.equals(Float.class)) {
						array = in.readArray(Float.class.getClassLoader());
					} else if (type.equals(Boolean.class)) {
						array = in.readArray(Boolean.class.getClassLoader());
					} else if (type.equals(String.class)) {
						for (int ii = 0; ii < length; ii++) {
							Array.set(array, ii, in.readString());
						}
					} else if (type.equals(Date.class)) {
						for (int ii = 0; ii < length; ii++) {
							long ll = in.readLong();
							Date date = ll >= 0 ? new Date(ll) : null;
							Array.set(array, ii, date);
						}
					} else if (type.isEnum()) {
						for (int ii = 0; ii < length; ii++) {
							String str = in.readString();
							Enum value = Enum.valueOf((Class<Enum>) type, str);
							Array.set(array, ii, value);
						}
					} else if (BHParcelable.class.isAssignableFrom(type)) {
						for (int ii = 0; ii < length; ii++) {
							Array.set(array, ii, in.readParcelable(type.getClassLoader()));
						}
						// } else if
						// (BHParcelable[].class.isAssignableFrom(type)) {
						// Parcelable[] values =
						// in.readParcelableArray(type.getComponentType().getClassLoader());
						// BHParcelable[] array = (BHParcelable[])
						// Array.newInstance(type.getComponentType(),
						// values.length);
						// for (int ii = 0; ii < values.length; ii++) {
						// Array.set(array, ii, values[ii]);
						// }
						// field.set(model, array);
						// field.set(model,
						// (BHParcelable[])in.readParcelableArray(type.getComponentType().getClassLoader()));
						// } else if (Collection.class.isAssignableFrom(type)) {
						// List list = new ArrayList();
						// in.readTypedList(list, BHParcelable.CREATOR);
						// field.set(model, list);
					} else {
						// wtf
						Log.e("BHParcelable", "Could not read field from parcel: " + field.getName() + " (" + type.toString() + ")");
					}
					field.set(model, array);
				}
				// else {
				// field.set(model, null);
				// }
			} else if (Collection.class.isAssignableFrom(type)) {
				type = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
				List list = new ArrayList();
				if (type.equals(Integer.class)) {
					in.readList(list, Integer.class.getClassLoader());
				} else if (type.equals(Long.class)) {
					in.readList(list, Long.class.getClassLoader());
				} else if (type.equals(Double.class)) {
					in.readList(list, Double.class.getClassLoader());
				} else if (type.equals(Float.class)) {
					in.readList(list, Float.class.getClassLoader());
				} else if (type.equals(Boolean.class)) {
					in.readList(list, Boolean.class.getClassLoader());
				} else if (type.equals(String.class)) {
					in.readStringList(list);
				} else if (type.equals(Date.class)) {
					int length = in.readInt();
					for (int ii = 0; ii < length; ii++) {
						long ll = in.readLong();
						list.add(ll > 0 ? new Date(ll) : null);
					}
				} else if (type.isEnum()) {
					int length = in.readInt();
					for (int ii = 0; ii < length; ii++) {
						String str = in.readString();
						Enum value = Enum.valueOf((Class<Enum>) type, str);
						list.add(value);
					}
				} else if (BHParcelable.class.isAssignableFrom(type)) {
					in.readTypedList(list, BHParcelable.CREATOR);
				} else {
					// wtf
					Log.e("BHParcelable", "Could not write field to parcel: " + " (" + type.toString() + ")");
				}

				field.set(model, list);
			} else {
				if (type.equals(int.class)) {
					field.set(model, in.readInt());
				} else if (type.equals(long.class)) {
					field.set(model, in.readLong());
				} else if (type.equals(double.class)) {
					field.set(model, in.readDouble());
				} else if (type.equals(float.class)) {
					field.set(model, in.readFloat());
				} else if (type.equals(boolean.class)) {
					field.set(model, in.readInt() == 1);
				} else if (type.equals(Integer.class)) {
					field.set(model, in.readValue(Integer.class.getClassLoader()));
				} else if (type.equals(Long.class)) {
					field.set(model, in.readValue(Long.class.getClassLoader()));
				} else if (type.equals(Double.class)) {
					field.set(model, in.readValue(Double.class.getClassLoader()));
				} else if (type.equals(Float.class)) {
					field.set(model, in.readValue(Float.class.getClassLoader()));
				} else if (type.equals(Boolean.class)) {
					field.set(model, in.readValue(Boolean.class.getClassLoader()));
				} else if (type.equals(String.class)) {
					field.set(model, in.readString());
				} else if (type.equals(Date.class)) {
					long ll = in.readLong();
					Date date = ll >= 0 ? new Date(ll) : null;
					field.set(model, date);
				} else if (type.isEnum()) {
					String str = in.readString();
					Enum value = Enum.valueOf((Class<Enum>) type, str);
					field.set(model, value);
				} else if (BHParcelable.class.isAssignableFrom(type)) {
					field.set(model, in.readParcelable(type.getClassLoader()));
				} else {
					// wtf
					Log.e("BHParcelable", "Could not read field from parcel: " + field.getName() + " (" + type.toString() + ")");
				}
			}
		}
	}

	/*
	 * Comparator object for Members, Fields, and Methods
	 */
	@SuppressWarnings("unchecked")
	private static Comparator<Field> compareMemberByName = new BHParcelable.CompareMemberByName();

	@SuppressWarnings("rawtypes")
	private static class CompareMemberByName implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			String s1 = ((Member) o1).getName();
			String s2 = ((Member) o2).getName();

			if (o1 instanceof Method) {
				s1 += getSignature((Method) o1);
				s2 += getSignature((Method) o2);
			} else if (o1 instanceof Constructor) {
				s1 += getSignature((Constructor) o1);
				s2 += getSignature((Constructor) o2);
			}
			return s1.compareTo(s2);
		}
	}

	/**
	 * Compute the JVM signature for the class.
	 */
	private static String getSignature(Class<?> clazz) {
		String type = null;
		if (clazz.isArray()) {
			Class<?> cl = clazz;
			int dimensions = 0;
			while (cl.isArray()) {
				dimensions++;
				cl = cl.getComponentType();
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dimensions; i++) {
				sb.append("[");
			}
			sb.append(getSignature(cl));
			type = sb.toString();
		} else if (clazz.isPrimitive()) {
			if (clazz == Integer.TYPE) {
				type = "I";
			} else if (clazz == Byte.TYPE) {
				type = "B";
			} else if (clazz == Long.TYPE) {
				type = "J";
			} else if (clazz == Float.TYPE) {
				type = "F";
			} else if (clazz == Double.TYPE) {
				type = "D";
			} else if (clazz == Short.TYPE) {
				type = "S";
			} else if (clazz == Character.TYPE) {
				type = "C";
			} else if (clazz == Boolean.TYPE) {
				type = "Z";
			} else if (clazz == Void.TYPE) {
				type = "V";
			}
		} else {
			type = "L" + clazz.getName().replace('.', '/') + ";";
		}
		return type;
	}

	/*
	 * Compute the JVM method descriptor for the method.
	 */
	private static String getSignature(Method meth) {
		StringBuffer sb = new StringBuffer();

		sb.append("(");

		Class<?>[] params = meth.getParameterTypes(); // avoid copy
		for (int j = 0; j < params.length; j++) {
			sb.append(getSignature(params[j]));
		}
		sb.append(")");
		sb.append(getSignature(meth.getReturnType()));
		return sb.toString();
	}

	/*
	 * Compute the JVM constructor descriptor for the constructor.
	 */
	private static String getSignature(Constructor<?> cons) {
		StringBuffer sb = new StringBuffer();

		sb.append("(");

		Class<?>[] params = cons.getParameterTypes(); // avoid copy
		for (int j = 0; j < params.length; j++) {
			sb.append(getSignature(params[j]));
		}
		sb.append(")V");
		return sb.toString();
	}

	public static String toJSON(BHParcelable info) {
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.PRIVATE).serializeNulls().create();
		return gson.toJson(info);
	}

	public static <T> T fromJSON(String json, Class<T> cls) {
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.PRIVATE).serializeNulls().create();
		return gson.fromJson(json, cls);
	}
	
	public Object copy() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
