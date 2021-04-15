$(function() {
  $("#check_all_uw").change(function() {
    $(".uw-checkbox:checkbox")
      .not(this)
      .prop("checked", $(this).is(":checked"));
  });
});
