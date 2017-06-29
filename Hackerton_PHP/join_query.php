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

  $id = mysqli_real_escape_string($connect, $_POST['form_id']);
  $password = mysqli_real_escape_string($connect, $_POST['form_password']);
  $passwords = mysqli_real_escape_string($connect, base64_encode($password));
  $name = mysqli_real_escape_string($connect, $_POST['form_name']);
  $phone = mysqli_real_escape_string($connect, $_POST['form_phone']);
  $major = mysqli_real_escape_string($connect, $_POST['form_major']);
  $track = mysqli_real_escape_string($connect, $_POST['form_track']);
  $android = mysqli_real_escape_string($connect, $_POST['form_android']);
  $web = mysqli_real_escape_string($connect, $_POST['form_web']);
  $ios = mysqli_real_escape_string($connect, $_POST['form_ios']);
  $design = mysqli_real_escape_string($connect, $_POST['form_design']);

  mysqli_select_db($connect, "Member");
  $query_mysql="SELECT * FROM Member WHERE ID=BINARY('$id')";
  if ($result = mysqli_query($connect, $query_mysql)) {
    mysqli_select_db($connect, "Member");
    $query_mysql1="INSERT INTO Member(ID, PW, Name, Phone, MajorKey, TrackKey, AndroidFlag, WebFlag, IOSFlag, DesignFlag) VALUES('$id', '$passwords', '$name', '$phone', '$major', '$track', '$android', '$web', '$ios', '$design')";
    if ($result1 = mysqli_query($connect, $query_mysql1)) {
      echo "Authorized";
    } else {
      echo "Unauthorized(2)";
    }
  } else {
    echo "Unauthorized(3)";
  }

  mysqli_close($connect);
?>
