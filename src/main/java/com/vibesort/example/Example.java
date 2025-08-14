package com.vibesort.example;

import com.vibesort.Sort;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Example usage of VibeSort library
 */
public class Example {
    public static void main(String[] args) {
        // 1. 引入 vibesort 库 (通过 Maven 依赖)
        
        // 2. 创建一个 Sort 类的实例
        // 方式一：使用环境变量（推荐）
        // 需要设置以下环境变量：
        // - OPENAI_API_KEY: 必需，OpenAI API 密钥
        // - OPENAI_MODEL: 可选，模型名称（默认 gpt-3.5-turbo）
        // - OPENAI_BASE_URL: 可选，API 基础 URL（默认 OpenAI 官方 URL）
        
        try {
            Sort sort = new Sort(); // 自动从环境变量读取配置
            
            // 方式二：手动指定参数
            // Sort sort = new Sort("your-api-key");
            // Sort sort = new Sort("your-api-key", "gpt-4");
            // Sort sort = new Sort("your-api-key", "gpt-4", "https://your-custom-endpoint.com/v1/chat/completions");
            // 3. 调用 Sort 类的 sort 方法
            List<String> items = Arrays.asList("苹果", "香蕉", "樱桃", "葡萄", "橙子");
            
            // 基本排序（字母顺序）
            List<String> alphabeticalSort = sort.sort(items);
            System.out.println("字母顺序排序: " + alphabeticalSort);
            
            // 按长度排序
            List<String> lengthSort = sort.sort(items, "按字符长度从短到长");
            System.out.println("按长度排序: " + lengthSort);
            
            // 按颜色排序
            List<String> colorSort = sort.sort(items, "按颜色从浅到深");
            System.out.println("按颜色排序: " + colorSort);
            
        } catch (IllegalArgumentException e) {
            System.err.println("配置错误: " + e.getMessage());
            System.err.println("请确保设置了 OPENAI_API_KEY 环境变量");
        } catch (IOException e) {
            System.err.println("排序失败: " + e.getMessage());
        }
    }
}