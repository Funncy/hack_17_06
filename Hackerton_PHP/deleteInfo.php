<?php
  header("Content-Type: text/html; charset=UTF-8");

  $hostname = "localhost";
  $database = "mattmatt96";
  $username = "mattmatt96";
  $password = "tlawogns96";
  $connect = mysqli_connect($hostname, $username, $password, $database);
  if (!$connect) {
    die('데이터베이스 접근에 문제가 있습니다.' . mysqli_error($connect));
  }

  mysqli_set_charset($connect, "utf8");

  $sno = mysqli_real_escape_string($connect, $_POST['id']);
  $trackno = mysqli_real_escape_string($connect, $_POST['track']);
  $nodeno = mysqli_real_escape_string($connect, $_POST['node']);

  if (empty($sno)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else { // 접근 완료
    mysqli_select_db($connect, "TrackAttend");
    $query_mysql="DELETE FROM TrackAttend WHERE ID = '" . $sno . "' and TrackKey = '" . $trackno . "' and
                                                    LectureKey = '" . $nodeno . "'";
    if ($result = mysqli_query($connect, $query_mysql)) {
      echo "Authorized";
    } else {
      echo "Unauthorized";
    }
  }

mysqli_close($connect);
?>
