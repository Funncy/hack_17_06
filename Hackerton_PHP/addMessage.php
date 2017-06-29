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

  $sendid = mysqli_real_escape_string($connect, $_POST['sendid']);
  $recieveid = mysqli_real_escape_string($connect, $_POST['receiveid']);
  $type = mysqli_real_escape_string($connect, $_POST['type']);
  $text = mysqli_real_escape_string($connect, $_POST['text']);
  $date = date("20y-m-d");

  if (empty($sendid) || empty($recieveid)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else { // 접근 완료
    mysqli_select_db($connect, "Message");
    $query_mysql="INSERT INTO Message(SendID, ReceiveID, MesType, MesText, MesDate) VALUES('$sendid', '$recieveid',
      '$type', '$text', '$date')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      echo "Authorized";
    } else {
      echo "Unauthorized";
    }
  }

  mysqli_close($connect);
?>
