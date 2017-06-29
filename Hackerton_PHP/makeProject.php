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

  $id = mysqli_real_escape_string($connect, $_POST['id']);
  $name = mysqli_real_escape_string($connect, $_POST['name']);
  $introduce = mysqli_real_escape_string($connect, $_POST['introduce']);
  $date = date("20y-m-d");
  $android = mysqli_real_escape_string($connect, $_POST['android']);
  $web = mysqli_real_escape_string($connect, $_POST['web']);
  $ios = mysqli_real_escape_string($connect, $_POST['ios']);
  $design = mysqli_real_escape_string($connect, $_POST['design']);
  $recruit = 1;

  if (empty($id) || empty($name)) {
    echo "Unauthorized(1)";
  } else if($name == "") {
    echo "Unauthorized(2)";
  }else { // 접근 완료
    mysqli_select_db($connect, "Project");
    $query_mysql="INSERT INTO Project(ProName, ProLeader, ProDate, ProIntroduce, RecruitFlag, AndroidFlag, WebFlag, IOSFlag, DesignFlag)
     VALUES('$name', '$id', '$date', '$introduce', '$recruit','$android', '$web', '$ios', '$design')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      echo "Authorized";
    } else {
      echo "Unauthorized";
    }

    mysqli_select_db($connect, "Project");
    $query_mysql="SELECT * FROM Project WHERE ProLeader = BINARY('$id')";
    if ($result = mysqli_query($connect, $query_mysql)) {
        $result_each = mysqli_fetch_assoc($result);
        $prokey = $result_each['ProKey'];
    } else {
      $json_data = array (
        "result" => "Unauthorized(3)"
      );
      echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
    }

    mysqli_select_db($connect, "ProMembers");
    $query_mysql="INSERT INTO ProMembers(MemID, ProKey, LeaderFlag) VALUES('$id', '$prokey', '1')";
    if ($result = mysqli_query($connect, $query_mysql)) {

    } else {
      echo "Unauthorized";
    }
  }

mysqli_close($connect);
?>
