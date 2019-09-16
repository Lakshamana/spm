package br.ufpa.labes.spm.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;

public class UtilReflection {

  public static Object invocarMetodo(Object objetoDoMetodo, String nomeDoMetodo, Object parametro) {
    return invokeMethod(objetoDoMetodo, nomeDoMetodo, new Object[] {parametro});
  }

  // parametro nao eh collection, mas precisamos a partir do parameter type descobrir se eh preciso
  // ou na opopular uma collection para invocar o metodo
  public static Object invocarMetodoSetpecial(
      Object objetoDoMetodo, String nomeDoMetodo, Object parametros) {
    try {
      Class[] para_types = getClasseDoParameter(objetoDoMetodo.getClass(), nomeDoMetodo);
      if (containsCollectionType(para_types)) {
        // transform set Operation in get Operation
        String getMethodName = nomeDoMetodo.substring(3, nomeDoMetodo.length());
        getMethodName = "get" + getMethodName;
        Method toInvoke = objetoDoMetodo.getClass().getMethod(getMethodName, null);
        Collection collection = (Collection) toInvoke.invoke(objetoDoMetodo, null);
        collection.add(parametros);
        Method metodo =
            objetoDoMetodo
                .getClass()
                .getMethod(
                    nomeDoMetodo, getClasseDoParameter(objetoDoMetodo.getClass(), nomeDoMetodo));
        return metodo.invoke(objetoDoMetodo, collection);
      }
      // else do as we where doing before
      Method metodo =
          objetoDoMetodo
              .getClass()
              .getMethod(
                  nomeDoMetodo, getClasseDoParameter(objetoDoMetodo.getClass(), nomeDoMetodo));
      return metodo.invoke(objetoDoMetodo, parametros);
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }

  private static boolean containsCollectionType(Class[] para_types) {
    for (int i = 0; i < para_types.length; i++) {
      if (para_types[i].getName().equals(Collection.class.getName())) {
        return true;
      }
    }
    return false;
  }

  public static Object invokeMethodOld(
      Object objetoDoMetodo, String nomeDoMetodo, Object[] parametros) {
    try {
      Method metodo =
          objetoDoMetodo
              .getClass()
              .getMethod(
                  nomeDoMetodo, getClasseDoParameter(objetoDoMetodo.getClass(), nomeDoMetodo));
      return metodo.invoke(objetoDoMetodo, parametros);
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }

  /*	public static Object invokeMethod(Object objetoDoMetodo,String nomeDoMetodo, Object[] parametros) {
  		try {
  			Method metodo = objetoDoMetodo.getClass().getMethod(nomeDoMetodo,getClasseDoParametro(objetoDoMetodo.getClass(),nomeDoMetodo, parametros));
  			System.out.println("Classe do M�todo: "+objetoDoMetodo.getClass().getSimpleName());
  			System.out.println("M�todo �: "+metodo);
  			return metodo.invoke(objetoDoMetodo,parametros);
  		} catch (Throwable e) {
  			e.printStackTrace();
  			return null;
  		}
  	}
  */
  public static Object invokeMethod(
      Object objetoDoMetodo, String nomeDoMetodo, Object[] parametros) {
    Method[] methods = objetoDoMetodo.getClass().getMethods();
    Class[] parameter_types = null;
    for (int i = 0; i < methods.length; i++) {
      if (methods[i].getName().equals(nomeDoMetodo)) {
        parameter_types = methods[i].getParameterTypes();

        try {
          if (parametros == null) {
            if (parameter_types.length == 0) {
              return methods[i].invoke(objetoDoMetodo, parametros);
            }
          } else if (parameter_types.length == parametros.length) {

            for (int j = 0; j < parametros.length; j++) {
              Class type = parameter_types[j];
              Object param = parametros[j];
              if (type.isInstance(param) || type.equals(param.getClass())) {
                return methods[i].invoke(objetoDoMetodo, parametros);
              }
            }
          }
        } catch (IllegalArgumentException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public static Object invokeMethod(
      Class classeDoMetodo, String nomeDoMetodo, Object[] parametros) {
    try {
      Method metodo =
          classeDoMetodo.getMethod(
              nomeDoMetodo, getClasseDoParameter(classeDoMetodo, nomeDoMetodo));
      return metodo.invoke(classeDoMetodo, parametros);
    } catch (Throwable e) {
      return null;
    }
  }

  public static String getMethodNameFromParameter(Class parameter, Object obj) {

    if (parameter == null || obj == null) {
      return null;
    }
    // try to find the correct class
    Method[] methods = obj.getClass().getMethods();
    for (int method_count = 0; method_count < methods.length; method_count++) {
      Class[] parameter_type = methods[method_count].getParameterTypes();
      for (int parameter_cont = 0; parameter_cont < parameter_type.length; parameter_cont++) {
        if (parameter_type[parameter_cont].getName().equals(parameter.getName())) {
          return methods[method_count].getName();
        } else {
          String superTry = Translator.getSuper(parameter.getName());
          if (superTry != null) {
            if (superTry.equals(parameter_type[parameter_cont].getName()))
              return methods[method_count].getName();
          }
        }
      }
    }

    // try to find the correct class using super classes definition --> after improve optmization of
    // this source

    Class[] other_types = parameter.getClasses();

    for (int type_count = 0; type_count < other_types.length; type_count++) {
      Method[] methods1 = other_types[type_count].getMethods();
      for (int method_count = 0; method_count < methods.length; method_count++) {
        Class[] parameter_type = methods1[method_count].getParameterTypes();
        for (int parameter_cont = 0; parameter_cont < parameter_type.length; parameter_cont++) {
          if (parameter_type[parameter_cont].getName().equals(other_types[type_count].getName())) {
            return methods1[method_count].getName();
          }
        } // fim for
      } // fim for
    } // fim for

    return null;
  }

  public static String getMethodNameFromContext(String obj, String relationshipName) {
    // try to find the correct class

    return Translator.getBackOperation(obj, relationshipName);
  }

  public static Class[] getClassesFromObjects(Object[] objects) {
    if (objects == null) {
      return null;
    }
    Class[] classes = new Class[objects.length];
    for (int i = 0; i < classes.length; i++) {
      classes[i] = getClasseDoParametro(objects, i);
    }
    return classes;
  }

  private static Class[] getClasseDoParameter(Class classeDoMetodo, String nomeDoMetodo) {
    Method[] methods = classeDoMetodo.getMethods();
    Class[] parameter_types = null;
    for (int i = 0; i < methods.length; i++) {
      if (methods[i].getName().equals(nomeDoMetodo)) {
        parameter_types = methods[i].getParameterTypes();
        break;
      }
    }
    return parameter_types;
  }

  private static Class getClasseDoParametro(Object[] objetos, int i) {
    if (objetos[i] instanceof Map) {
      return Collection.class;
    } else if (objetos[i] instanceof List) {
      return Collection.class;
    } else if (objetos[i] instanceof Set) {
      return Collection.class;
    } else {
      if (objetos[i] != null) {
        return objetos[i].getClass();
      } else {
        return Object.class;
      }
    }
  }

  public static String getClassObjectName(Class clazz) {
    return getClassObjectName(clazz.getName().trim());
  }

  public static String getClassObjectName(String className) {
    StringTokenizer token = new StringTokenizer(className, ".");
    String result = new String();
    while (token.hasMoreTokens()) {
      result = token.nextToken();
    }
    return result;
  }

  public static Object[] copyGenericArrayValues(
      String parameterClassName, Object[] values, int arraySize) {
    try {
      Class<?> parameterClass = Class.forName(parameterClassName);
      Object parameterArrayInstance = Array.newInstance(parameterClass, arraySize);

      for (int i = 0; i < arraySize; i++) {

        Object notInParameterValue = values[i];

        Object parameterCorrectValue = parameterClass.newInstance();

        BeanUtils.copyProperties(parameterCorrectValue, notInParameterValue);

        Array.set(parameterArrayInstance, i, parameterCorrectValue);
      }

      Object[] arrayInstanceInArray = (Object[]) parameterArrayInstance;

      return arrayInstanceInArray;

      // production code should handle these exceptions more gracefully
    } catch (ClassNotFoundException x) {
      x.printStackTrace();
    } catch (IllegalAccessException x) {
      x.printStackTrace();
    } catch (InstantiationException x) {
      x.printStackTrace();
    } catch (InvocationTargetException x) {
      x.printStackTrace();
    }
    return null;
  }
}
