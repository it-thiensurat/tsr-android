package th.co.bighead.utilities;

import android.util.Log;

import com.google.gson.Gson;

import org.kobjects.isodate.IsoDate;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.data.controller.TransactionLogController;
import th.co.thiensurat.data.info.TransactionLogInfo;
import th.co.thiensurat.service.TransactionService;

public abstract class BHService {
    protected class MarshalDouble implements Marshal {
        public MarshalDouble() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) throws IOException, XmlPullParserException {

            return Double.parseDouble(parser.nextText());
        }

        @Override
        public void register(SoapSerializationEnvelope cm) {
            cm.addMapping(cm.xsd, "double", Double.class, this);

        }

        @Override
        public void writeInstance(XmlSerializer writer, Object obj) throws IOException {
            writer.text(obj.toString());
        }
    }

    protected class MarshalFloat implements Marshal {
        public MarshalFloat() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) throws IOException, XmlPullParserException {

            return Float.parseFloat(parser.nextText());
        }

        @Override
        public void register(SoapSerializationEnvelope cm) {
            cm.addMapping(cm.xsd, "double", Float.class, this);

        }

        @Override
        public void writeInstance(XmlSerializer writer, Object obj) throws IOException {
            writer.text(obj.toString());
        }
    }

    protected class MarshalDate implements Marshal {
        public MarshalDate() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) throws IOException, XmlPullParserException {

            return IsoDate.stringToDate(parser.nextText(), IsoDate.DATE_TIME);

        }

        @Override
        public void register(SoapSerializationEnvelope cm) {
            cm.addMapping(cm.xsd, "DateTime", Date.class, this);

        }

        @Override
        public void writeInstance(XmlSerializer writer, Object obj) throws IOException {
            writer.text(IsoDate.dateToString((Date) obj, IsoDate.DATE_TIME));
        }

    }

    protected abstract String serviceUrl();

    protected String namespace() {
        return "http://tempuri.org/";
    }

    protected Element headers() {
        return null;
    }


    protected Element userHeaders() {
        return null;
    }


    protected int timeOut() {
        return 20000;
    }

    private SoapObject serialize(Object info, String childName) {
        SoapObject obj = new SoapObject(namespace(), childName);
        if (info == null) {
            return obj;
        }

        Class<?> type = info.getClass();
        if (type.isArray()) {
            type = type.getComponentType();
            if (type.equals(int.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("int", Array.getInt(info, ii));
                }
            } else if (type.equals(long.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("long", Array.getLong(info, ii));
                }
            } else if (type.equals(Long.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("long", Array.getLong(info, ii));
                }
            } else if (type.equals(double.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("double", Array.getDouble(info, ii));
                }
            } else if (type.equals(float.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("float", Array.getFloat(info, ii));
                }
            } else if (type.equals(boolean.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("boolean", Array.getBoolean(info, ii));
                }
            } else if (type.equals(String.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("string", Array.get(info, ii));
                }
            } else if (type.equals(Date.class)) {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addProperty("dateTime", getDateString(Array.get(info, ii)));
                }
            } else {
                for (int ii = 0; ii < Array.getLength(info); ii++) {
                    obj.addSoapObject(serialize(Array.get(info, ii), type.getSimpleName()));
                }
            }
        } else if (Collection.class.isAssignableFrom(type)) {
        } else {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                type = field.getType();
                String name = field.getName();
                try {
                    if (type.isArray()) {
                        obj.addSoapObject(serialize(field.get(info), name));
                    } else if (Collection.class.isAssignableFrom(type)) {
                    } else {
                        if (type.equals(int.class)) {
                            obj.addProperty(name, field.getInt(info));
                        } else if (type.equals(long.class)) {
                            obj.addProperty(name, field.getLong(info));
                        } else if (type.equals(Long.class)) {
                            obj.addProperty(name, field.getLong(info));
                        } else if (type.equals(double.class)) {
                            obj.addProperty(name, field.getDouble(info));
                        } else if (type.equals(float.class)) {
                            obj.addProperty(name, field.getFloat(info));
                        } else if (type.equals(boolean.class)) {
                            obj.addProperty(name, field.getBoolean(info));
                        } else if (type.equals(String.class)) {
                            obj.addProperty(name, field.get(info));
                        } else if (type.equals(Date.class)) {
                            obj.addProperty(name, getDateString(field.get(info)));
                        } else {
                            obj.addSoapObject(serialize(field.get(info), name));
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return obj;
    }

    private String getDateString(final Object date) {
        if (date != null && date instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
        } else {
            return "0001-01-01T00:00:00";
        }
    }

    private int getInt(final Object soap) {
        if (soap != null && soap.getClass().equals(SoapPrimitive.class)) {
            SoapPrimitive pre = (SoapPrimitive) soap;
            return Integer.parseInt(pre.toString());
        } else if (soap != null && soap instanceof Number) {
            return (Integer) soap;
        }

        return 0;
    }

    private long getLong(final Object soap) {
        if (soap != null && soap.getClass().equals(SoapPrimitive.class)) {
            SoapPrimitive pre = (SoapPrimitive) soap;
            return Long.parseLong(pre.toString());
        } else if (soap != null && soap instanceof Number) {
            return (Long) soap;
        }

        return 0;
    }

    private double getDouble(final Object soap) {
        if (soap != null && soap.getClass().equals(SoapPrimitive.class)) {
            SoapPrimitive pre = (SoapPrimitive) soap;
            return Double.parseDouble(pre.toString());
        } else if (soap != null && soap instanceof Number) {
            return (Double) soap;
        }

        return 0;
    }

    private float getFloat(final Object soap) {
        if (soap != null && soap.getClass().equals(SoapPrimitive.class)) {
            SoapPrimitive pre = (SoapPrimitive) soap;
            return Float.parseFloat(pre.toString());
        } else if (soap != null && soap instanceof Number) {
            return (Float) soap;
        }

        return 0;
    }

    private boolean getBoolean(final Object soap) {
        if (soap != null && soap.getClass().equals(SoapPrimitive.class)) {
            SoapPrimitive pre = (SoapPrimitive) soap;
            return Boolean.parseBoolean(pre.toString());
        } else if (soap != null && soap instanceof Boolean) {
            return (Boolean) soap;
        }

        return false;
    }

    private String getString(final Object soap) {
        if (soap != null && soap.getClass().equals(SoapPrimitive.class)) {
            SoapPrimitive pre = (SoapPrimitive) soap;
            return pre.toString();
        } else if (soap != null && soap instanceof String) {
            return (String) soap;
        }

        return null;
    }

    private Date getDate(final Object soap) {
        String dateString = "";
        if (soap != null && soap.getClass().equals(SoapPrimitive.class)) {
            SoapPrimitive pre = (SoapPrimitive) soap;
            dateString = pre.toString();
        } else if (soap != null && soap instanceof String) {
            dateString = (String) soap;
        }

        if (!dateString.equals("")) {
            int index = dateString.indexOf(".");
            if (index >= 0) {
                dateString = dateString.substring(0, index);
            }

            return IsoDate.stringToDate(dateString, IsoDate.DATE_TIME);
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
    private void deserialize(Object obj, final Object soap) {
        Field[] fields = obj.getClass().getFields();
        for (Field field : fields) {
            int modifier = field.getModifiers();
            if (Modifier.isStatic(modifier) || Modifier.isFinal(modifier)) {
                continue;
            }

            if (!((SoapObject) soap).hasProperty(field.getName())) {
                continue;
            }

            Object so = ((SoapObject) soap).getProperty(field.getName());

            Class<?> type = field.getType();
            if (type.isArray()) { // Array type;
                type = type.getComponentType();
                SoapObject ss = (SoapObject) ((SoapObject) soap).getProperty(field.getName());
                int length = 0;
                for (int ii = 0; ii < ss.getPropertyCount(); ii++) {
                    if (ss.getProperty(ii) == null) {
                        break;
                    }

                    length++;
                }

                Object array = Array.newInstance(type, length);
                if (type.equals(int.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.setInt(array, ii, getInt(ss.getProperty(ii)));
                    }
                } else if (type.equals(long.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.setLong(array, ii, getLong(ss.getProperty(ii)));
                    }
                } else if (type.equals(Long.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.setLong(array, ii, getLong(ss.getProperty(ii)));
                    }
                } else if (type.equals(double.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.setDouble(array, ii, getDouble(ss.getProperty(ii)));
                    }
                } else if (type.equals(float.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.setFloat(array, ii, getFloat(ss.getProperty(ii)));
                    }
                } else if (type.equals(boolean.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.setBoolean(array, ii, getBoolean(ss.getProperty(ii)));
                    }
                } else if (type.equals(String.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.set(array, ii, getString(ss.getProperty(ii)));
                    }
                } else if (type.equals(Date.class)) {
                    for (int ii = 0; ii < length; ii++) {
                        Array.set(array, ii, getDate(ss.getProperty(ii)));
                    }
                } else {
                    for (int ii = 0; ii < length; ii++) {
                        Array.set(array, ii, deserialize(type, (SoapObject) ss.getProperty(ii)));
                    }
                }

                try {
                    field.set(obj, array);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (Collection.class.isAssignableFrom(type)) {
                try {
                    type = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    SoapObject ss = (SoapObject) ((SoapObject) soap).getProperty(field.getName());
                    int length = 0;
                    for (int ii = 0; ii < ss.getPropertyCount(); ii++) {
                        if (ss.getProperty(ii) == null) {
                            break;
                        }

                        length++;
                    }

                    Object list = new ArrayList();
                    Method add = List.class.getDeclaredMethod("add", Object.class);
                    if (type.equals(Integer.class)) {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, Integer.valueOf(getInt(ss.getProperty(ii))));
                        }
                    } else if (type.equals(Long.class)) {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, Long.valueOf(getLong(ss.getProperty(ii))));
                        }
                    } else if (type.equals(Double.class)) {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, Double.valueOf(getDouble(ss.getProperty(ii))));
                        }
                    } else if (type.equals(Float.class)) {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, Float.valueOf(getFloat(ss.getProperty(ii))));
                        }
                    } else if (type.equals(Boolean.class)) {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, Boolean.valueOf(getBoolean(ss.getProperty(ii))));
                        }
                    } else if (type.equals(String.class)) {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, getString(ss.getProperty(ii)));
                        }
                    } else if (type.equals(Date.class)) {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, getDate(ss.getProperty(ii)));
                        }
                    } else {
                        for (int ii = 0; ii < length; ii++) {
                            add.invoke(list, deserialize(type, (SoapObject) ss.getProperty(ii)));
                        }
                    }

                    field.set(obj, list);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    if (type.equals(int.class)) {
                        field.setInt(obj, getInt(so));
                    } else if (type.equals(long.class)) {
                        field.setLong(obj, getLong(so));
                    } else if (type.equals(Long.class)) {
                        field.setLong(obj, getLong(so));
                    } else if (type.equals(double.class)) {
                        field.setDouble(obj, getDouble(so));
                    } else if (type.equals(float.class)) {
                        field.setFloat(obj, getFloat(so));
                    } else if (type.equals(boolean.class)) {
                        field.setBoolean(obj, getBoolean(so));
                    } else if (type.equals(String.class)) {
                        field.set(obj, getString(so));
                    } else if (type.equals(Date.class)) {
                        field.set(obj, getDate(so));
                    } else {
                        field.set(obj, deserialize(type, (SoapObject) so));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private <T> T deserialize(Class<T> cls, final SoapObject soap) {
        try {
            T result = cls.newInstance();
            deserialize(result, soap);
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    protected <T> T call(String methodName, Object methodData, Class<T> cls) {
        return call(methodName, methodData, cls, false);
    }

    protected <T> T call(String methodName, Object methodData, Class<T> cls, boolean isSchedule) {
        return call(methodName, "info", methodData, cls, isSchedule, false);
//        return call(methodName, "info", methodData, cls, isSchedule);
    }

    protected <T> T call(String methodName, Object methodData, Class<T> cls, boolean isSchedule, boolean isSkipTransactionsLog) {
        return call(methodName, "info", methodData, cls, isSchedule, isSkipTransactionsLog);
    }

    protected <T> T call(String methodName, String rootName, Object methodData, Class<T> cls, boolean isSchedule, boolean isSkipTransactionsLog) {

        if(BHPreference.IsAdmin() && !(
                                        methodName.equals("GetUserByUserName")
                                        || methodName.equals("GetCurrentFortnight")
                                        || methodName.equals("GetDeviceMenus")
                                        || methodName.equals("SynchDataFromServer2Local")
                                        || methodName.equals("GetSignatureImages")
                                    )){
            return (T) null;
        }

        if (isSchedule) {
            Gson gson = new Gson();
            TransactionLogInfo trans = new TransactionLogInfo();
            trans.ServiceName = methodName;
            trans.ServiceInputName = rootName;
            trans.ServiceInputType = methodData != null ? methodData.getClass().getName() : null;
            trans.ServiceOutputType = cls != null ? cls.getName() : null;
            trans.ServiceInputData = gson.toJson(methodData);
            trans.SyncStatus = false;
            new TransactionLogController().addTransactionLog(trans);

            if (!isSkipTransactionsLog)
                TransactionService.registerSchedule(BHApplication.getContext());

            return (T) null;
        }

        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.encodingStyle = SoapEnvelope.XSD;
        soapEnvelope.skipNullProperties = true;
        SoapObject soapReq = new SoapObject(namespace(), methodName);

        if (methodData != null) {
            SoapObject obj = serialize(methodData, rootName);
            soapReq.addSoapObject(obj);
        }

        soapEnvelope.setOutputSoapObject(soapReq);
        MarshalDouble md = new MarshalDouble();
        md.register(soapEnvelope);
        MarshalFloat mf = new MarshalFloat();
        mf.register(soapEnvelope);
        MarshalDate mt = new MarshalDate();
        mt.register(soapEnvelope);
        if (headers() != null) {
            soapEnvelope.headerOut = new Element[1];
            soapEnvelope.headerOut[0] = headers();

            if (userHeaders() != null) {
                Element[] newHeaderOut = Arrays.copyOf(soapEnvelope.headerOut, soapEnvelope.headerOut.length + 1);
                newHeaderOut[newHeaderOut.length - 1] = userHeaders();
                soapEnvelope.headerOut = newHeaderOut;
            }
        }
        String soapAction = namespace() + methodName;
        HttpTransportSE httpTransport = new HttpTransportSE(serviceUrl(), timeOut());
//		ArrayList<HeaderProperty> headers = new ArrayList<HeaderProperty>();
//		headers.add(new HeaderProperty("Connection", "close"));
//		httpTransport.call(soapAction, soapEnvelope, headers);
//        Log.e("service url", serviceUrl());
        try {
            httpTransport.debug = true;
//            try {
//                Log.i("service", "call round 1 error at method name : " + methodName);
//                httpTransport.call(soapAction, soapEnvelope);
//            } catch (Exception e) {
//                /*// TODO: handle exception
//                Log.i("service", "call round 2 error at method name : " + methodName);
////				e.printStackTrace();
//                Thread.sleep(1000);
//                httpTransport.call(soapAction, soapEnvelope);*/
//            }
//            Log.e("service", "call round 1 error at method name : " + methodName);
            httpTransport.call(soapAction, soapEnvelope);

//            Log.e("dump request: ", httpTransport.requestDump);
//            Log.e("dump response: ", httpTransport.responseDump);

            Object retObj = soapEnvelope.bodyIn;
//            Log.e("Log object", String.valueOf(retObj));
            if (retObj instanceof SoapFault) {
                SoapFault fault = (SoapFault) retObj;
            } else {
                SoapObject result = (SoapObject) retObj;
                if (result.getPropertyCount() > 0) {
                    SoapObject obj = (SoapObject) result.getProperty(0);
                    return deserialize(cls, obj);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Sync call", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        /*** [START] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ   ***/
        MainActivity.checkTransactionLog();
        /*** [END] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ  ***/
        return null;
    }

}
