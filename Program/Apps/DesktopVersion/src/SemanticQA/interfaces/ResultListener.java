package SemanticQA.interfaces;

/**
 * Listener utama untuk memonitor hasil proses final
 * @author syamsul
 */
public interface ResultListener {
    
    public void onSuccess(String answer);
    public void onFail(String reason);
    
}
