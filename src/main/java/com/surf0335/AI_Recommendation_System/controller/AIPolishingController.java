package com.surf0335.AI_Recommendation_System.controller;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.auth.Auth;
import com.baidubce.qianfan.model.chat.ChatResponse;
import com.baidubce.qianfan.model.completion.CompletionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: 加更多填表的内容 数字签名 模板导出（LaTeX/word）

@Controller
public class AIPolishingController {
    //@Autowired
    //private AIPolishService polishService;

    @Value("${etherpad.api.url}")
    private String etherpadApiUrl;

    @Value("${etherpad.api.key}")
    private String etherpadApiKey;

    @Value("${baidu.ai.api.access}")
    private String baiduAccessKey;

    @Value("${baidu.ai.api.secret}")
    private String baiduApiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private String tmpLetterContent = null;

    private final String prompt = "请根据以下要求和信息给 %s ( %s ) 生成一封海外留学研究生的英文推荐信:首先，\n" +
            "推荐信正式开始后，第一段用70-80字描述：与推荐人认识的时间: %s, 在什么情况下认识的: %s \n" +
            "第二和第三段用110字描述：推荐人的性格：%s ,在这种性格下再推荐主要推荐的能力: %s ,这些能力通过以下例子进行佐证: %s 。注意，这一段描述要基于推荐人老师的背景: %s \n" +
            "然后第四段总结对同学各项能力的肯定，并且期望让学校考虑录取" +
            "注意，推荐信语言风格是: %s \n 学生所申请的专业是：%s \n " +
            "要注意，请只生成推荐信的主体，不要生成开始的问候和后面的落款，只生成上面提及的几段，也不要生成一些其他的废话";


    private final ExecutorService executor = Executors.newCachedThreadPool();
    @GetMapping("/ai_polish")
    public String getAiPolishPage(Model m){
        return "AiPolishing";
    }

    @GetMapping("/ai_prompt")
    public SseEmitter getAiPrompt(@RequestParam String message) {
        SseEmitter emitter = new SseEmitter();
        Qianfan qianfan = new Qianfan(Auth.TYPE_OAUTH, baiduAccessKey, baiduApiSecret);
        executor.execute(() -> {
            try {
                qianfan.chatCompletion()
                        .model("ERNIE-4.0-8K")
                        .addMessage("user", prompt)
                        .addMessage("assistant",tmpLetterContent)
                        .addMessage("user",message)
                        .executeStream()
                        .forEachRemaining(chunk -> {
                            try {
                                String result = chunk.getResult();
                                System.out.print("Received chunk: " + result); // 输出到控制台
                                emitter.send(SseEmitter.event().data(chunk.getResult()));
                            } catch (IOException e) {
                                e.printStackTrace();
                                emitter.completeWithError(e);
                            }
                        });
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @PostMapping("/generate_letter")
    @ResponseBody
    public Map<String, String> generateLetter(@RequestParam Map<String, String> formData) {
        System.out.println("收到请求： " + formData);
        // 2. 调用百度 AI API 获取推荐信内容
        String content = generateContentWithBaiduAi(formData);
        // String content = "generateContentWithBaiduAi(formData)";
        tmpLetterContent = content;

        // 3. 将内容插入到 Etherpad 文档
        String padId = generateRandomPadId();
        createEtherpadDocument(padId,content);

        // 4. 返回 Etherpad 文档 ID
        return Map.of("padId", padId);
    }

    @PostMapping("/createPad")
    public ResponseEntity<Map<String, String>> createPad(@RequestParam(value = "padId", required = false) String padId,
                                                         @RequestParam(value = "content", required = false) String content) {
        if (padId == null || padId.isEmpty()) {
            padId = generateRandomPadId(); // 生成随机 padId
        }

        boolean padExists = checkIfPadExists(padId); // 检查 padId 是否已经存在

        // 如果 padId 已存在，直接返回现有 pad 的 URL
        if (padExists) {
            Map<String, String> result = new HashMap<>();
            result.put("padId", padId);
            result.put("url", etherpadApiUrl.replace("/api", "/p/") + padId);
            return ResponseEntity.ok(result);
        }

        // 如果 padId 不存在，则创建新的文档
        String createdPadId = createEtherpadDocument(padId, content);

        Map<String, String> result = new HashMap<>();
        result.put("padId", createdPadId);
        result.put("url", etherpadApiUrl.replace("/api", "/p/") + createdPadId);

        return ResponseEntity.ok(result);
    }

    private boolean checkIfPadExists(String padId) {
        // 这里你可以调用 Etherpad 的 API 来检查 padId 是否存在
        String checkPadUrl = etherpadApiUrl + "/1/getRevisionsCount?apikey=" + etherpadApiKey + "&padID=" + padId;
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(checkPadUrl, Map.class);
            // 如果响应成功且 code 为 0，则表示 pad 存在
            return response.getStatusCode().is2xxSuccessful() && ((Integer) response.getBody().get("code")) == 0;
        } catch (Exception e) {
            // 捕获异常，表示 pad 不存在
            return false;
        }
    }




    private String generateContentWithBaiduAi(Map<String, String> formData) {
        String recommendeeName = formData.get("recommendeeName");
        String gender = formData.get("gender");
        String personality =  formData.get("personality");
        String acquaintanceTime = formData.get("acquaintanceTime");
        String acquaintanceEvent = formData.get("acquaintanceEvent");
        String abilities = formData.get("abilities");
        String examples = formData.get("examples");
        String refereeDetails = formData.get("refereeDetails");
        String languageLevel = formData.get("languageLevel");
        String major = formData.get("major");
        // 组合成prompt
        String prompt = generatePrompt(
        recommendeeName,gender,personality,acquaintanceTime, acquaintanceEvent, abilities, examples, refereeDetails, languageLevel, major);
        // 调用百度AI API生成推荐信内容
        Qianfan qianfan = new Qianfan(Auth.TYPE_OAUTH, baiduAccessKey, baiduApiSecret);
        ChatResponse response = qianfan.chatCompletion()
                .model("ERNIE-4.0-8K")
                .addMessage("user", prompt)
                .temperature(0.7)
                .execute();
        String result = response.getResult();
        System.out.println(result);
        return result;
    }

    private String generatePrompt(String recommendeeName,String gender,String personality, String acquaintanceTime, String acquaintanceEvent,
                                  String abilities, String examples, String refereeDetails, String languageLevel,  String major) {
        String result = String.format(prompt,recommendeeName,gender,personality,
                acquaintanceTime, acquaintanceEvent,
                abilities, examples, refereeDetails, languageLevel, major);
        System.out.println(result);
        return result;
    }
    private String generateRandomPadId() {
        // 生成唯一的 padID
        return "pad_" + System.currentTimeMillis();
    }
    private String createEtherpadDocument(String padID , String content) {
        System.out.println("createEtherpadDocument");
        String etherpadCreateApiUrl = etherpadApiUrl + "/1/createPad";
        String createPadRequestUrl = etherpadCreateApiUrl + "?padID=" + padID + "&apikey=" + etherpadApiKey;;
        String authorId = "1";

        content = "padID: " + padID + "\n\n" + content;
        // 创建 Etherpad 文档
        try {
            // 创建请求体
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            if (content != null) {
                requestBody.add("text", content);
            }
            if (authorId != null) {
                requestBody.add("authorId", authorId);
            }

            // 执行请求
            Map<String, Object> createResponse = restTemplate.postForObject(createPadRequestUrl, requestBody, Map.class);

            if (createResponse == null) {
                throw new RuntimeException("Failed to create pad: response is null");
            }

            // 处理响应
            Integer code = (Integer) createResponse.get("code");
            if (code == 0) {
                // 成功
                System.out.println("创建成功");
                return padID;
            } else {
                // 错误
                String message = (String) createResponse.get("message");
                throw new RuntimeException("Error creating pad: " + message);
            }
        } catch (HttpClientErrorException e) {
            // 记录详细的错误信息
            System.err.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        }
    }



}
