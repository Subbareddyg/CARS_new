resv_data = {
	ptsRequired 	: 0,
	ptsAvailable 	: 0,
	ptsUseYear	 	: "",
	ptsBorrowed 	: 0,
	ptsRented 		: 0,
	ptsRentedCostPerUnit : 0.005,
	ptsCost : function() {
		return this.ptsRented * this.ptsRentedCostPerUnit;
	},
	ptsNeeded : function() {
		return(this.ptsRequired - this.ptsAvailable - this.ptsBorrowed - this.ptsRented);
	},
	ptsTotal : function() {
		return this.ptsBorrowed + this.ptsRented;
	},
	
	creditsRequired : 0,
	creditsAvailable : 0,
	creditsBorrowed : 0,
	creditsRented : 0,
	creditsRentedCostPerUnit : 1.5,
	creditsCost : function() {
		return this.creditsRented * this.creditsRentedCostPerUnit;
	},
	creditsNeeded : function() {
		return this.creditsRequired - this.creditsAvailable - this.creditsBorrowed - this.creditsRented;
	},
	creditsTotal : function() {
		return this.creditsBorrowed + this.creditsRented;
	},
		
	transactionsRequired : 0,
	transactionsAvailable : 0,
	transactionsCostPerItem : 25,
	transactionsNeeded : function() {
		var difference = this.transactionsRequired - this.transactionsAvailable;
		if(difference > 0) {
			return difference
		}
		return 0;
	},
	transactionsCost : function() {
		return this.transactionsNeeded() * this.transactionsCostPerItem;
	},
	resvCost : function() {
		return this.ptsCost() + this.creditsCost() + this.transactionsCost() + this.guestCost();
	},
	
	ccType : null,
	ccNum : null,
	ccExpDate : null,
	
	guestNum : 0,
	guestName : null,
	guestAddress1 : null,
	guestAddress2 : null,
	guestCity : null,
	guestState : null,
	guestZip : null,
	guestCostPerPerson : 25,
	guestCost : function() {
		return this.guestNum * this.guestCostPerPerson;
	},
	
	loadData : function() {
		this.ptsRequired = Number(getQueryVariable('pr'));
		this.ptsAvailable = Number(getQueryVariable('pa'));
		this.ptsUseYear = getQueryVariable('puy');
		this.ptsBorrowed = Number(getQueryVariable('pb'));
		this.ptsRented = Number(getQueryVariable('prt'));
			
		this.creditsRequired = Number(getQueryVariable('cr'));
		this.creditsAvailable = Number(getQueryVariable('ca'));
		this.creditsBorrowed = Number(getQueryVariable('cb'));
		this.creditsRented = Number(getQueryVariable('crt'));
			
		this.transactionsRequired = Number(getQueryVariable('tr'));
		this.transactionsAvailable = Number(getQueryVariable('ta'));
		this.transactionsCostPerItem = Number(getQueryVariable('tc'));
		
		this.ccType = getQueryVariable('cctype');
		this.ccNum = getQueryVariable('ccnum');
		this.ccExpDate = getQueryVariable('ccdate');
		
		this.guestNum = getQueryVariable('gnum');
		this.guestName = getQueryVariable('gn');
		this.guestAddress1 = getQueryVariable('ga1');
		this.guestAddress2 = getQueryVariable('ga2');
		this.guestCity = getQueryVariable('gc');
		this.guestState = getQueryVariable('gs');
		this.guestZip = getQueryVariable('gz');
	},
	saveData : function() {
		var queryStr = "";
		
		queryStr += "&pr=" + this.ptsRequired;
		queryStr += "&pa=" + this.ptsAvailable;
		queryStr += "&puy=" + this.ptsUseYear;
		queryStr += "&pb=" + this.ptsBorrowed;
		queryStr += "&prt=" + this.ptsRented;
			
		queryStr += "&cr=" + this.creditsRequired;
		queryStr += "&ca=" + this.creditsAvailable;
		queryStr += "&cb=" + this.creditsBorrowed;
		queryStr += "&crt=" + this.creditsRented;
			
		queryStr += "&tr=" + this.transactionsRequired;
		queryStr += "&ta=" + this.transactionsAvailable;
		queryStr += "&tc=" + this.transactionsCostPerItem;
		
		queryStr += "&cctype=" + this.ccType;
		queryStr += "&ccnum=" + this.ccNum;
		queryStr += "&ccdate=" + this.ccExpDate;
			
		queryStr += "&gnum=" + this.guestNum;
		queryStr += "&gn=" + this.guestName;
		queryStr += "&ga1=" + this.guestAddress1;
		queryStr += "&ga2=" + this.guestAddress2;
		queryStr += "&gc=" + this.guestCity;
		queryStr += "&gs=" + this.guestState;
		queryStr += "&gz=" + this.guestZip;
		
		return queryStr;
	}					
}

/* Reference Article:
Dustin Diaz:
http://www.dustindiaz.com/top-ten-javascript/
*/

/* addEvent: simplified event attachment */
function addEvent( obj, type, fn ) {
	if (obj.addEventListener) {
		obj.addEventListener( type, fn, false );
		EventCache.add(obj, type, fn);
	}
	else if (obj.attachEvent) {
		obj["e"+type+fn] = fn;
		obj[type+fn] = function() { obj["e"+type+fn]( window.event ); }
		obj.attachEvent( "on"+type, obj[type+fn] );
		EventCache.add(obj, type, fn);
	}
	else {
		obj["on"+type] = obj["e"+type+fn];
	}
}
	
var EventCache = function(){
	var listEvents = [];
	return {
		listEvents : listEvents,
		add : function(node, sEventName, fHandler){
			listEvents.push(arguments);
		},
		flush : function(){
			var i, item;
			for(i = listEvents.length - 1; i >= 0; i = i - 1){
				item = listEvents[i];
				if(item[0].removeEventListener){
					item[0].removeEventListener(item[1], item[2], item[3]);
				};
				if(item[1].substring(0, 2) != "on"){
					item[1] = "on" + item[1];
				};
				if(item[0].detachEvent){
					item[0].detachEvent(item[1], item[2]);
				};
				item[0][item[1]] = null;
			};
		}
	};
}();
addEvent(window,'unload',EventCache.flush);

/* window 'load' attachment */
function addLoadEvent(func) {
	var oldonload = window.onload;
	
	if (typeof window.onload != 'function') {
		window.onload = func;
	}
	else {
		window.onload = function() {
			oldonload();
			func();
		}
	}
}

function getElementsByParent(node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	return els[0];
}

/* grab Elements from the DOM by className */
function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

/* toggle an element's display */
function toggle(obj) {
	var el = document.getElementById(obj);
	if ( el.style.display != 'none' ) {
		el.style.display = 'none';
	}
	else {
		el.style.display = '';
	}
}

/* insert an element after a particular node */
function insertAfter(parent, node, referenceNode) {
	parent.insertBefore(node, referenceNode.nextSibling);
}

/* Array prototype, matches value in array: returns bool */
Array.prototype.inArray = function (value) {
	var i;
	for (i=0; i < this.length; i++) {
		if (this[i] === value) {
			return true;
		}
	}
	return false;
};

/* get, set, and delete cookies */
function getCookie( name ) {
	var start = document.cookie.indexOf( name + "=" );
	var len = start + name.length + 1;
	if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {
		return null;
	}
	if ( start == -1 ) return null;
	var end = document.cookie.indexOf( ";", len );
	if ( end == -1 ) end = document.cookie.length;
	return unescape( document.cookie.substring( len, end ) );
}
	
function setCookie( name, value, expires, path, domain, secure ) {
	var today = new Date();
	today.setTime( today.getTime() );
	if ( expires ) {
		expires = expires * 1000 * 60 * 60 * 24;
	}
	var expires_date = new Date( today.getTime() + (expires) );
	document.cookie = name+"="+escape( value ) +
		( ( expires ) ? ";expires="+expires_date.toGMTString() : "" ) + //expires.toGMTString()
		( ( path ) ? ";path=" + path : "" ) +
		( ( domain ) ? ";domain=" + domain : "" ) +
		( ( secure ) ? ";secure" : "" );
}
	
function deleteCookie( name, path, domain ) {
	if ( getCookie( name ) ) document.cookie = name + "=" +
			( ( path ) ? ";path=" + path : "") +
			( ( domain ) ? ";domain=" + domain : "" ) +
			";expires=Thu, 01-Jan-1970 00:00:01 GMT";
}

/* quick getElement reference */
function $() {
	var elements = new Array();
	for (var i = 0; i < arguments.length; i++) {
		var element = arguments[i];
		if (typeof element == 'string')
			element = document.getElementById(element);
		if (arguments.length == 1)
			return element;
		elements.push(element);
	}
	return elements;
}

function getQueryVariable(variable) {
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if (pair[0] == variable) {
      return unescape(pair[1]);
    }
  } 
  return null;
  // alert('Query Variable ' + variable + ' not found');
}

var popUpHelp=0;
function popUpHelpWindow(helpText)
{
	var width = 520;
	var height = 600;
	var URLStr = "/Help.shtml";
	
	if(!helpText) helpText = "";
	
	if(popUpHelp)
	{
	if(!popUpHelp.closed) popUpHelp.close();
	}
	popUpHelp = open(URLStr + "?helptext=" + escape(helpText), 'popUpHelp', 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,copyhistory=yes,width='+width+',height='+height);
}


var phone_field_length=0;
function TabNext(obj,event,len,next_field) {
	if (event == "down") {
		phone_field_length=obj.value.length;
		}
	else if (event == "up") {
		if (obj.value.length != phone_field_length) {
			phone_field_length=obj.value.length;
			if (phone_field_length == len) {
				next_field.focus();
				}
			}
		}
	}

