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

  if (empty($id)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else {
    mysqli_select_db($connect, "Member");
    $query_mysql="SELECT * FROM Member WHERE ID = BINARY('$id')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      if (mysqli_num_rows($result) == 1) {
        $result_each = mysqli_fetch_assoc($result);

        $json_data = array (
          "result" => "Authorized",
          "id" => $result_each["ID"],
          "name" => $result_each["Name"],
          "phone" => $result_each["Phone"],
          "major" => $result_each["MajorKey"],
          "track" => $result_each["TrackKey"],
          "introduce" => $result_each["Introduce"]
        );
        echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
      }
    } else {
      $json_data = array (
        "result" => "Unauthorized(3)"
      );
      echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
    }

    echo "/";
    mysqli_select_db($connect, "Portfolio_Text");
    $query_mysql="SELECT * FROM Portfolio_Text WHERE MemID = BINARY('$id')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      $number = mysqli_num_rows($result);
      echo "$number";
      echo ",";
      if ($number > 0) {
        while($ro = mysqli_fetch_assoc($result)){
           echo $ro ['Text'];
           echo ",";
        }
      } else {
        echo "empty";
      }
    }

    echo "/";
    mysqli_select_db($connect, "Portfolio_Image");
    $query_mysql="SELECT * FROM Portfolio_Image WHERE MemID = BINARY('$id')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      $number = mysqli_num_rows($result);
      echo "$number";
      echo ",";
      if ($number > 0) {
        while($rows = mysqli_fetch_assoc($result)){
           echo $rows ['Image'];
           echo ",";
        }
      } else {
        echo "empty";
      }
    }
  }

  mysqli_close($connect);
?>
