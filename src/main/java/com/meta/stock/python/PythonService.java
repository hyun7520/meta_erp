package com.meta.stock.python;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PythonService {

    @Value("${app.python.exec-path}")
    private String pythonExecPath; // yml에서 설정한 파이썬 실행 경로

    private File renderPythonFile() {
        // =========================================================
        // 1. 파이썬 실행 파일 경로 찾기 (절대 경로 변환)
        // =========================================================
        File pythonFile;
        File potentialPath = new File(pythonExecPath);
        if (potentialPath.isAbsolute()) {
            pythonFile = potentialPath;
        } else {
            String projectRoot = System.getProperty("user.dir");
            pythonFile = Paths.get(projectRoot, pythonExecPath).toFile();
        }

        if (!pythonFile.exists()) {
            throw new RuntimeException("파이썬 실행 파일을 찾을 수 없습니다: " + pythonFile.getAbsolutePath());
        }
        System.out.println("✅ 파이썬 실행 경로: " + pythonFile.getAbsolutePath());
        return pythonFile;
    }


    // 수요량 예측 연산 python 실행
    public String runDemandsPython() {
        try {
            File pythonFile = renderPythonFile();
            // =========================================================
            // 2. 리소스 파일들을 임시 폴더로 복사 (Script + CSV)
            // =========================================================

            // (1) 파이썬 스크립트 복사 (src/main/resources/python/predict_demand.py)
            File scriptFile = copyResourceToTemp("python/predict_demand.py", "predict_demand", ".py");

            // (2) CSV 파일 복사 (src/main/resources/file/폴더 안에 있는 파일명 입력)
            // ★주의: 실제 파일명이 'data.csv'라면 아래 이름을 그에 맞게 수정하세요!
            File csvFile = copyResourceToTemp("file/product_demands.csv", "demand_data", ".csv");
            // 만약 파일이 여러 개라면 위와 같이 계속 추가해서 복사하면 됩니다.
            // File csvFile2 = copyResourceToTemp("file/다른파일.csv", "other", ".csv");

            List<String> command = new ArrayList<>();
            command.add(pythonFile.getAbsolutePath());
            command.add(scriptFile.getAbsolutePath());
            command.add(csvFile.getAbsolutePath()); // sys.argv[1]로 전달됨
            // command.add(csvFile2.getAbsolutePath()); // 파일이 더 있다면 추가
            return saveFile(command, "result_demand.csv");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // 로스율 연산 python 실행
    public String runLossPython() {
        try {
            File pythonFile = renderPythonFile();
            // =========================================================
            // 2. 리소스 파일들을 임시 폴더로 복사 (Script + CSV)
            // =========================================================

            // (1) 파이썬 스크립트 복사 (src/main/resources/python/loss_concat.py)
            File scriptFile = copyResourceToTemp("python/loss_concat.py", "loss_concat", ".py");

            // (2) CSV 파일 복사 (src/main/resources/file/폴더 안에 있는 파일명 입력)
            // ★주의: 실제 파일명이 'data.csv'라면 아래 이름을 그에 맞게 수정하세요!
            File csvFile1 = copyResourceToTemp("file/future_material_date.csv", "input_data", ".csv");
            File csvFile2 = copyResourceToTemp("file/past_material_date.csv", "input_data", ".csv");
            File csvFile3 = copyResourceToTemp("file/product_loss_predic.csv", "input_data", ".csv");

            // 만약 파일이 여러 개라면 위와 같이 계속 추가해서 복사하면 됩니다.
            // File csvFile2 = copyResourceToTemp("file/다른파일.csv", "other", ".csv");

            // =========================================================
            // 3. ProcessBuilder 실행
            // =========================================================
            // 명령어 구성: [python.exe] [스크립트경로] [CSV경로]
            List<String> command = new ArrayList<>();
            command.add(pythonFile.getAbsolutePath());
            command.add(scriptFile.getAbsolutePath());
            command.add(csvFile1.getAbsolutePath()); // sys.argv[1]로 전달됨
            command.add(csvFile2.getAbsolutePath()); // sys.argv[1]로 전달됨
            command.add(csvFile3.getAbsolutePath()); // sys.argv[1]로 전달됨
            // command.add(csvFile2.getAbsolutePath()); // 파일이 더 있다면 추가
            return saveFile(command, "final_result.csv");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 리소스 폴더의 파일을 시스템 임시 폴더로 복사하는 헬퍼 메서드
     */
    private File copyResourceToTemp(String resourcePath, String tempPrefix, String tempSuffix) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            throw new IOException("리소스 파일을 찾을 수 없습니다: " + resourcePath);
        }

        // 임시 파일 생성
        Path tempPath = Files.createTempFile(tempPrefix, tempSuffix);

        // 복사 (덮어쓰기 옵션)
        Files.copy(resource.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);

        // JVM 종료 시 파일 삭제 (선택 사항)
        // tempPath.toFile().deleteOnExit();

        return tempPath.toFile();
    }

    private String saveFile(List<String> command, String saveRoot) throws IOException, InterruptedException {
        // =========================================================
        // 3. ProcessBuilder 실행
        // =========================================================
        // 명령어 구성: [python.exe] [스크립트경로] [CSV경로]
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // =========================================================
        // 4. 결과 읽기
        // =========================================================
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR")); // 한글 깨짐 시 "EUC-KR" 추가 고려
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println("[Python] " + line);
            output.append(line).append("\n");
        }
        reader.close();

        int exitCode = process.waitFor();
        System.out.println("Python Process 종료 코드: " + exitCode);

        if (exitCode != 0) {
            return "Error (Exit Code " + exitCode + ")";
        }

        if (exitCode == 0) {
            String csvResult = output.toString();

            String projectRoot = System.getProperty("user.dir");

            // 2. 저장할 파일의 상대 경로를 설정합니다.
            String relativePath = "src/main/resources/file/" + saveRoot;

            // 3. 절대 경로를 생성합니다.
            String savePath = Paths.get(projectRoot, relativePath).toString();

            try {
                // 파일로 저장
                Files.write(Paths.get(savePath), csvResult.getBytes(StandardCharsets.UTF_8));
                System.out.println("✅ 결과 파일이 저장되었습니다: " + savePath);
            } catch (IOException e) {
                System.out.println("❌ 파일 저장 실패: " + e.getMessage());
            }
            return csvResult; // 컨트롤러나 서비스에는 텍스트로 반환
        }

        return "Success";
    }
}