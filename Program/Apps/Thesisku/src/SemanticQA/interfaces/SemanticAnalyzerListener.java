package SemanticQA.interfaces;

import java.util.List;

/**
 * interface untuk memonitor proses pembentukan parse tree
 * @author syamsul
 */
public interface SemanticAnalyzerListener {
    
    void onAnalyzeSuccess(List<Object> parseTree);
    void onAnalyzeFail(String reason);
    
}
