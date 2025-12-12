<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새 일기 작성</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/write.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">

<!-- Sidebar script -->
<script>
    function openSidebar() {
        document.getElementById("sidebar").classList.add("open");
        document.getElementById("sidebarBackdrop").classList.add("show");
    }
    function closeSidebar() {
        document.getElementById("sidebar").classList.remove("open");
        document.getElementById("sidebarBackdrop").classList.remove("show");
    }
</script>

<!-- Summernote CSS -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.20/summernote-lite.min.css" rel="stylesheet">
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- Summernote JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.20/summernote-lite.min.js"></script>
</head>

<body>

<!-- SIDEBAR -->
<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

<!-- TOPBAR -->
<div class="topbar">
    <button class="menu-btn" type="button" onclick="openSidebar()">☰</button>
    <div class="topbar-title">New Entry</div>
</div>

<!-- CONTENT -->
<div class="page">
    <div class="container">
        <h2>📝 새 일기 작성</h2>

        <form id="diaryForm" action="<%=request.getContextPath()%>/diary" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="insert">

            <!-- 날짜 -->
            <div class="label">날짜</div>
            <input class="input" type="text" name="diaryDate" placeholder="예: 2025-11-27" required>

            <!-- 제목 -->
            <div class="label">제목</div>
            <input class="input" type="text" name="title" placeholder="오늘의 제목" required>

            <!-- 사진 업로드 -->
            <div class="label">사진 업로드</div>
            <input class="input" type="file" id="photoInput" accept="image/*">

            <!-- 내용 -->
            <div class="label">내용</div>
<textarea id="summernote" name="content"></textarea>
<script>
    $('#summernote').summernote({
        placeholder: '오늘 있었던 일들을 편하게 적어보세요 :)',
        height: 280,
        lang: 'ko-KR',
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['font', ['fontsize', 'color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['insert', ['picture', 'link']],
            ['view', ['fullscreen']]
        ]
    });
</script>

            <!-- BUTTONS -->
            <button class="btn-save" type="button" onclick="saveDiary()">저장하기</button>

            <a href="<%=request.getContextPath()%>/diary?action=list">
                <button class="btn-back" type="button">취소</button>
            </a>
        </form>

        <a href="<%=request.getContextPath()%>/diary?action=list" class="back-link">◀ 돌아가기</a>
    </div>
</div>

<!-- ============================== -->
<!--       OFFLINE SAVE LOGIC       -->
<!-- ============================== -->

<script>
// ===============================
//   📸 1) BASE64 IMAGE PREPARE
// ===============================
let offlineImageBase64 = null;

document.getElementById("photoInput").addEventListener("change", function(e) {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function(event) {
        offlineImageBase64 = event.target.result;
        console.log("📸 Base64 준비됨");
    };
    reader.readAsDataURL(file);
});


// ===============================
//   📝 2) SAVE OFFLINE DIARY
// ===============================
function saveOfflineDiary() {
    const date = document.querySelector("input[name='diaryDate']").value;
    const title = document.querySelector("input[name='title']").value;
    const content = $('#summernote').summernote('code');

    const diary = {
        id: "offline-" + Date.now(),
        date: date,
        title: title,
        content: content,
        imageBase64: offlineImageBase64,
        createdAt: new Date().toISOString()
    };

    let list = JSON.parse(localStorage.getItem("offline_diaries") || "[]");
    list.push(diary);
    localStorage.setItem("offline_diaries", JSON.stringify(list));

    alert("🌙 오프라인 모드: 임시 저장되었습니다!");
}


// ===============================
//   💾 3) SAVE BUTTON HANDLER
// ===============================
function saveDiary() {
    if (!navigator.onLine) {
        console.log("❌ 인터넷 없음 → 오프라인 저장");
        saveOfflineDiary();
        return;
    }

    document.getElementById("diaryForm").submit();
}


// ===============================
//   🗑 4) REMOVE FROM LOCALSTORAGE
// ===============================
function removeOfflineDiary(id) {tak sdelala chto dalyshe delty >

    let list = JSON.parse(localStorage.getItem("offline_diaries") || "[]");
    list = list.filter(item => item.id !== id);
    localStorage.setItem("offline_diaries", JSON.stringify(list));
}


// ===============================
//   🔄 5) SYNC OFFLINE DIARIES
// ===============================
function syncOfflineDiaries() {
    let list = JSON.parse(localStorage.getItem("offline_diaries") || "[]");

    if (list.length === 0) {
        console.log("동기화할 오프라인 일기 없음");
        return;
    }

    alert("🔄 인터넷 연결됨! 오프라인 일기 서버로 전송합니다...");

    list.forEach(async (diary) => {
        let formData = new FormData();
        formData.append("action", "offlineSync");
        formData.append("diaryDate", diary.date);
        formData.append("title", diary.title);
        formData.append("content", diary.content);
        formData.append("imageBase64", diary.imageBase64);

        let response = await fetch("<%=request.getContextPath()%>/diary", {
            method: "POST",
            body: formData
        });

        if (response.ok) {
            console.log("✔ 동기화 성공:", diary.id);
            removeOfflineDiary(diary.id);
        } else {
            console.log("❌ 동기화 실패:", diary.id);
        }
    });
}


// ===============================
//   🌐 6) AUTO-SYNC ON INTERNET RETURN
// ===============================
window.addEventListener("online", function() {
    console.log("🌐 온라인 상태! → 동기화 시작");
    syncOfflineDiaries();
});
</script>


</body>
</html>