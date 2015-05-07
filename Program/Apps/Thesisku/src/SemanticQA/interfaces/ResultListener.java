package SemanticQA.interfaces;

/**
 * Listener utama untuk memonitor hasil proses final
 * @author syamsul
 */
public interface ResultListener {
    
    void onSuccess(String answer);
    void onFail(String reason);
    
}
