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
  $connect1 = mysqli_connect($hostname, $username, $password, $database);
  if (!$connect1) {
    die('데이터베이스 접근에 문제가 있습니다.' . mysqli_error($connect1));
  }
  mysqli_set_charset($connect1, "utf8");

  $id = mysqli_real_escape_string($connect, $_POST['id']);

  if (empty($id)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else { // 접근 완료
    mysqli_select_db($connect, "Message");
    $query_mysql="SELECT * FROM Message WHERE ReceiveID = BINARY('$id')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      $number = mysqli_num_rows($result);
      echo $number;
      echo ",";
      if ($number > 0) {
        while($row = mysqli_fetch_assoc($result)){
          echo $row["MesText"];
          echo ",";
          $sendID = $row["SendID"];
          echo $sendID;
          echo ",";
          echo $row["MesType"];
          echo ",";

          mysqli_select_db($connect1, "ProMembers");
          $query_mysql1="SELECT * FROM ProMembers WHERE MemID = BINARY('$sendID')";
          if ($result1 = mysqli_query($connect1, $query_mysql1)) {
            $number1 = mysqli_num_rows($result1);
            $row1 = mysqli_fetch_assoc($result1);
            echo $row1['ProKey'];
            echo ",";
          } else {
            echo "Unauthorized";
          }
        }
      } else {
        echo "empty2";
      }
    } else {
      echo "Unauthorized";
    }
  }

  mysqli_close($connect);
?>
