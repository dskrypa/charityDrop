package resources.misc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;

public class Util {
	public static <T> T getBean(final String beanName, final Class<T> clazz){
		FacesContext fc = FacesContext.getCurrentInstance();
		T bean = fc.getApplication().evaluateExpressionGet(fc, "#{"+beanName+"}", clazz);
		return bean;
	}
	
	public static String hash(final String str){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(str.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < hash.length; i++){
				sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
		
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> col) throws ClassCastException {
		List<T> r = new ArrayList<T>(col.size());
		for(Object obj: col){
			r.add(clazz.cast(obj));
		}
		return r;
	}
}