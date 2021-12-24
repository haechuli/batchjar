package com.pthana.demo.util;

import com.pthana.demo.config.DataSourceConfiguration;
import com.pthana.demo.util.mapper.MapperUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/** 객체의 유틸 클래스
 *  Class 로딩 등의 객체관련 기능을 제공하는 유틸이다.
 *
 * @FileName : ObjectUtil.java
 * @Project  : tifs
 * @Date     : 2017. 11. 23.
 * @author   : SonJooArm
 * @Change History :
 * @Description :
 */
@Slf4j
public class ObjectUtil {


//
//
//    /**
//     * 클래스명으로 객체를 로딩한다.
//     * @param className
//     * @return
//     * @throws ClassNotFoundException
//     * @throws Exception
//     */
//    public static Class<?> loadClass(String className)
//            throws ClassNotFoundException, Exception {
//
//        Class<?> clazz = null;
//        try {
//            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
//        } catch (ClassNotFoundException e) {
//            throw new ClassNotFoundException();
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//
//        if (clazz == null) {
//            clazz = Class.forName(className);
//        }
//
//        return clazz;
//
//    }
//
//    /**
//     * 클래스명으로 객체를 로드한 후 인스턴스화 한다.
//     * @param className
//     * @return
//     * @throws ClassNotFoundException
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     * @throws Exception
//     */
//    public static Object instantiate(String className)
//            throws ClassNotFoundException, InstantiationException,
//            IllegalAccessException, Exception {
//        Class<?> clazz;
//
//        try {
//            clazz = loadClass(className);
//            return clazz.newInstance();
//        } catch (ClassNotFoundException e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is can not instantialized.");
//            throw new ClassNotFoundException();
//        } catch (InstantiationException e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is can not instantialized.");
//            throw new InstantiationException();
//        } catch (IllegalAccessException e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is not accessed.");
//            throw new IllegalAccessException();
//        } catch (Exception e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is not accessed.");
//            throw new Exception(e);
//        }
//    }
//
//    /**
//     * 클래스명으로 파라매터가 있는 클래스의 생성자를 인스턴스화 한다. 예) Class <?>
//     * clazz = EgovObjectUtil.loadClass(this.mapClass);
//     * Constructor <?> constructor =
//     * clazz.getConstructor(new Class
//     * []{DataSource.class, String.class}); Object []
//     * params = new Object []{getDataSource(),
//     * getUsersByUsernameQuery()};
//     * this.usersByUsernameMapping =
//     * (EgovUsersByUsernameMapping)
//     * constructor.newInstance(params);
//     * @param className
//     * @return
//     * @throws ClassNotFoundException
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     * @throws Exception
//     */
//    public static Object instantiate(String className, String[] types,
//            Object[] values) throws ClassNotFoundException,
//            InstantiationException, IllegalAccessException, Exception {
//        Class<?> clazz;
//        Class<?>[] classParams = new Class[values.length];
//        Object[] objectParams = new Object[values.length];
//
//        try {
//            clazz = loadClass(className);
//
//            for (int i = 0; i < values.length; i++) {
//                classParams[i] = loadClass(types[i]);
//                objectParams[i] = values[i];
//            }
//
//            Constructor<?> constructor = clazz.getConstructor(classParams);
//            return constructor.newInstance(values);
//
//        } catch (ClassNotFoundException e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is can not instantialized.");
//            throw new ClassNotFoundException();
//        } catch (InstantiationException e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is can not instantialized.");
//            throw new InstantiationException();
//        } catch (IllegalAccessException e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is not accessed.");
//            throw new IllegalAccessException();
//        } catch (Exception e) {
//            if (log.isErrorEnabled())
//                log.error(className + " : Class is not accessed.");
//            throw new Exception(e);
//        }
//    }
//
//    /**
//     * 객체가 Null 인지 확인한다.
//     * @param object
//     * @return Null인경우 true / Null이 아닌경우 false
//     */
//    public static boolean isNull(Object object) {
//        return ((object == null) || object.equals(null));
//    }
//
    @SuppressWarnings("rawtypes")
    public static Map convertObjectToMap(Object obj){

        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();



        for(int i=0; i <fields.length; i++){
            fields[i].setAccessible(true);
            try
            {
                map.put(fields[i].getName(), fields[i].get(obj));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return map;
    }


    /**
     * @Method Name : ConvertMapToObject
     * @Date    : 2018. 5. 7.
     * @Author  : Son Joo Arm
     * @Method Description :
     * @param map
     * @param obj
     * @return
     */
    public static Object ConvertMapToObject(Map<String,Object> map,Object obj){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator<String> itr = map.keySet().iterator();

        while(itr.hasNext())
        {
            keyAttribute = itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();

            for(int i=0;i<methods.length;i++)
            {
                if(methodString.equals(methods[i].getName()))
                {
                    try
                    {
                        if (methods[i].getParameterTypes()[0].getName() == "java.lang.Long")
                            methods[i].invoke(obj, Long.parseLong(map.get(keyAttribute).toString()));
                        else if (methods[i].getParameterTypes()[0].getName() == "java.math.BigDecimal")
                            methods[i].invoke(obj, new BigDecimal(map.get(keyAttribute).toString()));
                        else
                            methods[i].invoke(obj, map.get(keyAttribute));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }




    /**
     * @Method Name : copyObject
     * @Date    : 2018. 5. 7.
     * @Author  : Son Joo Arm
     * @Method Description : 소스오브젝트를 사용하여 타겟오브젝트를 신규생성함.
     * @param fromObject 소스오프젝트
     * @param clazz      타겟클래스
     * @return
     */
    public static <T> T copyObject(Object fromObject, Class<T> clazz)
    {
        try
        {


            String fromJson = MapperUtil.mapper.writeValueAsString(fromObject);


            return MapperUtil.mapper.readValue(fromJson, clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Method Name : copyObject
     * @Date    : 2018. 5. 7.
     * @Author  : Son Joo Arm
     * @Method Description : 소스오브젝트를 사용하여 타겟오브젝트를 신규생성함.
     * @param toObject 타겟오브젝트
     * @param fromObject 소스오브젝트
     */
    public static void copyObject(Object toObject , Object fromObject)
    {
        toObject = ObjectUtil.copyObject(fromObject, toObject.getClass());
    }

    /**
     * @Method Name : mergeObject
     * @Date    : 2018. 5. 7.
     * @Author  : Son Joo Arm
     * @Method Description : 소스오브젝트의 내용으로 타켓오프젝트의 필드 변경 - 기존객체 그대로 사용.
     * @param toObject    타켓오브젝트
     * @param fromObject  소스오브젝트
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static void mergeObject(Object toObject , Object fromObject) throws IllegalArgumentException, IllegalAccessException
    {
        if (toObject == null || fromObject == null) return;


        Map<String, Object> fdata = null ;
        Map<String, Object> tdata = null ;

        if(fromObject instanceof Map)
        {
            fdata = (Map<String, Object>) fromObject;
        }
        else
        {
            fdata = convertObjectToMap(fromObject);
        }


        if(toObject instanceof Map)
        {
            tdata = (Map<String, Object>) toObject;
        }
        else
        {
            tdata = convertObjectToMap(toObject);
        }

        if (fdata == null || tdata == null) return;

        for ( Map.Entry<String, Object> entry : fdata.entrySet())
        {
            if(tdata.containsKey(entry.getKey()))
            {
                tdata.replace(entry.getKey(), entry.getValue());
            }
            else
            {
                tdata.put(entry.getKey(), entry.getValue());
            }
        }

        if(toObject instanceof Map)
        {
            toObject = tdata;
        }
        else
        {
            String setMethodString = "set";
            String methodString = null;

            for(Map.Entry<String, Object> entry : fdata.entrySet() )
            {
                String key = entry.getKey();
                methodString = setMethodString+key.substring(0,1).toUpperCase()+key.substring(1);


                List<Method> mlist = Arrays.asList( toObject.getClass().getDeclaredMethods() );

                if(!(toObject.getClass().getSuperclass() instanceof Object))
                {
                    for(Method mt : toObject.getClass().getSuperclass().getDeclaredMethods())
                    {
                        //log.debug("SUPER {} merge Method={}", toObject.getClass().getSuperclass(),mt.getName());
                        mlist.add(mt);
                    }
                }

                for(Method ms : mlist)
                {
                    if(methodString.equals(ms.getName()))
                    {
                        Object val = null;
                        try
                        {
                            val = fdata.get(key);

                            //log.debug("methodString {} val {}-{}", methodString, val, val.getClass());

                            if(val != null)
                            {
                                Class<?>[] pTypes = ms.getParameterTypes();


                                if(pTypes == null ) continue;
                                if(pTypes.length == 0 ) continue;

                                Class<?> pt = pTypes[0];

                                if( List.class.isAssignableFrom( pt ) ) continue;

                                String sVal = fdata.get(key).toString();

                                try
                                {
                                    if ( !( pt.isAssignableFrom( tdata.get(key).getClass() )) )
                                    {
                                        if( LocalDate.class.isAssignableFrom( pt ) )
                                        {
                                            if (StringUtil.isBlank( sVal ))
                                                val = null;
                                            else
                                                val = LocalDate.parse(sVal, DataSourceConfiguration.DATE_FORMATTER);
                                        }
                                        else if( BigDecimal.class.isAssignableFrom( pt ) )
                                        {
                                            if (StringUtil.isBlank( sVal ))
                                                val = BigDecimal.ZERO;
                                            else
                                                val = new BigDecimal(sVal);

                                        }
                                        else if( ZonedDateTime.class.isAssignableFrom( pt ) )
                                        {
                                            if (StringUtil.isBlank( sVal ))
                                                val = null;
                                            else
                                                val = ZonedDateTime.parse(sVal);
                                        }
                                        else if( Long.class.isAssignableFrom( pt ) )
                                        {
                                            if (StringUtil.isBlank( sVal ))
                                                val = 0L;
                                            else
                                                val = Long.parseLong(sVal);
                                        }
                                        else if( Integer.class.isAssignableFrom( pt ) )
                                        {
                                            if (StringUtil.isBlank( sVal ))
                                                val = 0;
                                            else
                                                val = Integer.parseInt(sVal);
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    val = null;
                                }


                                //log.debug("pTypes {} {}", pTypes, val);
                            }


                            ms.invoke(toObject, val);
                        }
                        catch(Exception e)
                        {
                            log.error("mergeObject Error {} <-> {}", ms, toObject, val);
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }




    @SuppressWarnings("unchecked")
    public static <T> T getMapValue(Map<String,Object> map, String key, Class<T> clazz)
    {
        if(map.containsKey(key))
        {
            try
            {
                Object val = map.get(key);
                if( val != null)
                {
                    if( clazz.isAssignableFrom(val.getClass()) )
                    {
                        return clazz.cast(val);
                    }
                    else
                    {
                        if(val instanceof String)
                        {
                            if( BigDecimal.class.isAssignableFrom(clazz))
                                return clazz.cast(new BigDecimal(val.toString()));
                            else if( Long.class.isAssignableFrom(clazz))
                                return clazz.cast(Long.parseLong(val.toString()));
                            else if( Integer.class.isAssignableFrom(clazz))
                                return clazz.cast(Integer.parseInt(val.toString()));
                            else if( LocalDate.class.isAssignableFrom(clazz) )
                                //return (T) CmgDateUtil.stStringToLocalDate(map.get(key).toString());
                                return clazz.cast(Integer.parseInt(val.toString()));
                            else
                                return clazz.cast(val);
                        }
                    }
                }
                else
                {
                    return null;
                }
            }
            catch (Exception e)
            {
                if( BigDecimal.class.isAssignableFrom(clazz))
                    return clazz.cast("0");
                else if( Long.class.isAssignableFrom(clazz))
                    return clazz.cast("0");
                else if( Integer.class.isAssignableFrom(clazz))
                    return clazz.cast("0");
                else
                    return null;
            }
        }
        else
        {
            if( BigDecimal.class.isAssignableFrom(clazz))
                return clazz.cast("0");
            else if( Long.class.isAssignableFrom(clazz))
                return clazz.cast("0");
            else if( Integer.class.isAssignableFrom(clazz))
                return clazz.cast("0");
            else
                return null;
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static Object getObjectValue(Object obj, String key)
    {
        if(obj == null ) return null;
        if(obj instanceof Map)
        {
            Map mObj = (Map<String, Object>) obj;
            if(mObj.containsKey(key))
                return mObj.get(key);
            else
                return null;
        }
        else
        {
            String getMethodString = "get";
            String methodString = getMethodString + key.substring(0,1).toUpperCase() + key.substring(1);
            List<Method> mlist = Arrays.asList( obj.getClass().getDeclaredMethods() );
            for(Method ms : mlist)
            {
                if(methodString.equals(ms.getName()))
                {
                    try
                    {
                        return ms.invoke(obj);
                    }
                    catch(Exception e)
                    {
                        //e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void setValueObject(Object obj, String key, Object value)
    {
        if(obj instanceof Map)
        {
            Map<String, Object> mObj = (Map<String, Object>) obj;
            mObj.remove(key);
            mObj.put(key, value);
        }
        else
        {
            String setMethodString = "set";
            String methodString = setMethodString + key.substring(0,1).toUpperCase() + key.substring(1);
            List<Method> mlist = Arrays.asList( obj.getClass().getDeclaredMethods() );
            for(Method ms : mlist)
            {
                if(methodString.equals(ms.getName()))
                {
                    try
                    {
                        ms.invoke(obj, value);
                        break;
                    }
                    catch(Exception e)
                    {
                        //e.printStackTrace();
                    }
                }
            }
        }
    }


   

}
