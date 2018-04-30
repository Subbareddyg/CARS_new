/*
 * belk.cars.latecargroup.js
 * Belk CARS - Late CAR Group javascript
 * -rlr
 * Copyright Belk, Inc.
 */

$(document).ready(function() {
		$('#AddAssociationTemp').click(function() {
				$('#deptValues').val(seltdDeptValues);
				$('#VendorValues').val(seltedVendorValues);
				if (($('#deptValues').val().length == 0)
						&& ($('#VendorValues').val().length == 0)) {
					Ext.MessageBox.show({
						title : 'Error',
						msg : 'Please select items to associate',
						buttons : Ext.MessageBox.OK,
						icon : 'ext-mb-error'
					});
					return false;
				}
				$('#saveMyData').trigger('click');
		});

		var $ul = $('ul.depts_for_add');
		var seltdDeptValues = new Array();
		var seltedVendorValues = new Array();
		var count = 0;
		var myWord = 'departments';
		$("li").remove(":contains('OUTFIT_DEPARTMENT')");
		var $lis = $('li', $ul);
		$('#filter_results').html($lis.length + ' Departments');
		
		$('#txt_dept_filter').keyup(function() {
					var $this = $(this);
					$lis.hide();
					if ($this.val().length > 0) {
						$('#filter_results').html($(
									'li:contains("'
											+ $this.val().toUpperCase()
											+ '")', $ul).show().length
									+ ' Departments');
						;
					} else {
						$lis.show();
						$('#filter_results').html(
								$lis.length + ' Departments');
					}
				});

			$(":checkbox").click(function() {
				var ids = $(this).attr('id');
				var selectedId = $(this).attr('value');
				var isChecked = $(this).attr('checked');
				if (isChecked == true) {
					if (/departments/.test(ids)) {
						seltdDeptValues[count] = selectedId;
					} else {
						seltedVendorValues[count] = selectedId;
					}
					count++;
				} else {
					if (/departments/.test(ids)) {
						seltdDeptValues.remove(selectedId);
					} else {
						seltedVendorValues.remove(selectedId);
					}
				}
			});

			$('#chk_select_all').click(function() {
					if ($(this).is(':checked')) {
						$('input[type="checkbox"]:not(:checked)').attr(
								'checked', 'checked');
					} else {
						$('input[type="checkbox"]:checked').removeAttr(
								'checked');
					}
				});

			$('#search').click(function() {
				$('#lateCarVendorNoResults').hide();
				$('#searchValidateEr').hide();
				$('#selectedMethod').val('search');
				if (($('#vendorName').val().trim().length == 0)
						&& ($('#vendorNumber').val().trim().length == 0)) {
					$('#searchValidateEr').show();
					$('#vendorList').hide();
					$('#filterd').hide();
					return false;
				}
				$('#deptValues').val('');
				$('#VendorValues').val('');
			});

			$('#displayAll').click(function() {
				$('#selectedMethod').val('displayAll');
				$('#vendorName').val('');
				$('#vendorNumber').val('');
				$('#deptValues').val('');
				$('#VendorValues').val('');
			});
		});

$(document).ready(function() {   
			var $ul = $('ul.vendors_for_add');
			$("li").remove(":contains('OUTFIT_VENDOR')");
			var $lis = $('li', $ul);
			$('#filter_vendorResults').html($lis.length + ' Vendors');
			
			$('#txt_vendor_filter').keyup(function() {
						var $this = $(this);
						$lis.hide();
						if ($this.val().length > 0) {
							$('#filter_vendorResults').html($(
										'li:contains("'
												+ $this.val().toUpperCase()
												+ '")', $ul).show().length
										+ ' Vendors');
						} else {
							$lis.show();
							$('#filter_vendorResults').html(
									$lis.length + ' Vendors');
						}
					});
			if ($.browser.msie) {
			} else {
				$('#search').css("margin-top", "-20px");
				$('#displayAll').css("margin-top", "-20px");
			}
		});

function modalDialog(a) {
	$.ajaxSetup({
		cache : false
	});
	var removeId = $(a).attr("id");
	var n = removeId.split("_");
	var deptId = n[1];
	var lateCarsGroupid = n[2];
	var selectedMethod = 'remove';
	Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove? ',
			function(btn) {
				if (btn === 'yes') {
					$('#addAssociationDiv').load(
							"editLateCarsAssociationDetails.html?method=remove&lateCarsDeptId="
									+ deptId + "&lateCarsGroupId="
									+ lateCarsGroupid);
					return false;
				} else if (btn === 'no') {
					return false;
				}
			});
	return false;
}