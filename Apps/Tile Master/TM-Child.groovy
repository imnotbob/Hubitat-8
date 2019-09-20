/**
 *  ****************  Tile Master Child App  ****************
 *
 *  Design Usage:
 *  Create a tile with multiple devices and customization options.
 *
 *  Copyright 2019 Bryan Turcotte (@bptworld)
 * 
 *  This App is free.  If you like and use this app, please be sure to give a shout out on the Hubitat forums to let
 *  people know that it exists!  Thanks.
 *
 *  Remember...I am not a programmer, everything I do takes a lot of time and research!
 *  Donations are never necessary but always appreciated.  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://paypal.me/bptworld
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * ------------------------------------------------------------------------------------------------------------------------------
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @BPTWorld
 *
 *  App and Driver updates can be found at https://github.com/bptworld/Hubitat/
 *
 * ------------------------------------------------------------------------------------------------------------------------------
 *
 *  Changes:
 *
 *  V2.0.1 - 09/20/19 - Initial release.
 *  V2.0.0 - 08/18/19 - Now App Watchdog compliant
 *  V1.0.0 - 02/16/19 - Initially started working on this concept but never released.
 *
 */

def setVersion(){
	if(logEnable) log.debug "In setVersion - App Watchdog Child app code"
    // Must match the exact name used in the json file. ie. AppWatchdogParentVersion, AppWatchdogChildVersion
    state.appName = "TileMasterChildVersion"
	state.version = "v2.0.1"
    
    try {
        if(parent.sendToAWSwitch && parent.awDevice) {
            awInfo = "${state.appName}:${state.version}"
		    parent.awDevice.sendAWinfoMap(awInfo)
            if(logEnable) log.debug "In setVersion - Info was sent to App Watchdog"
	    }
    } catch (e) { log.error "In setVersion - ${e}" }
}

definition(
    name: "Tile Master Child",
    namespace: "BPTWorld",
    author: "Bryan Turcotte",
    description: "Create a tile with multiple devices and customization options.",
    category: "",
	parent: "BPTWorld:Tile Master",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
	importUrl: "",
)

preferences {
    page name: "pageConfig"
	page name: "line01Options", title: "", install: false, uninstall: true, nextPage: "pageConfig"
	page name: "line02Options", title: "", install: false, uninstall: true, nextPage: "pageConfig"
	page name: "line03Options", title: "", install: false, uninstall: true, nextPage: "pageConfig"
	page name: "line04Options", title: "", install: false, uninstall: true, nextPage: "pageConfig"
	page name: "line05Options", title: "", install: false, uninstall: true, nextPage: "pageConfig"
}

def pageConfig() {
    dynamicPage(name: "", title: "<h2 style='color:#1A77C9;font-weight: bold'>Tile Master</h2>", install: true, uninstall: true, refreshInterval:0) {
		display() 
        section("Instructions:", hideable: true, hidden: true) {
			paragraph "<b>Notes:</b>"
			paragraph "Create a tile with multiple devices and customization options."
		}
		section(getFormat("header-green", "${getImage("Blank")}"+" Choose which line configure")) {
			href "line01Options", title:"Line 1 Options", description:"Click here to setup line 1"
			href "line02Options", title:"Line 2 Options", description:"Click here to setup line 2"
			href "line03Options", title:"Line 3 Options", description:"Click here to setup line 3"
			href "line04Options", title:"Line 4 Options", description:"Click here to setup line 4"
			href "line05Options", title:"Line 5 Options", description:"Click here to setup line 5"
		}
        section(getFormat("header-green", "${getImage("Blank")}"+" Dashboard Tile")) {
            input(name: "tileDevice", type: "capability.actuator", title: "Vitual Device created to send the data to:", required: true, multiple: false)
        }
		section(getFormat("header-green", "${getImage("Blank")}"+" General")) {label title: "Enter a name for this automation", required: false}
		sampleTileHandler()
        section() {
            input(name: "logEnable", type: "bool", defaultValue: "true", title: "Enable Debug Logging", description: "Enable extra logging for debugging.")
		}
		display2()
	}
}

def line01Options(){
    dynamicPage(name: "line01Options", title: "Line 01 Options", install: false, uninstall: false){
		section(getFormat("header-green", "${getImage("Blank")}"+" Table Options")) {
			input "nSections01", "enum", title: "Number of Sections", required: true, multiple: false, options: ["1","2","3"], submitOnChange: true, width: 6
			input "border01", "enum", title: "Show a Border for Testing", required: true, multiple: false, options: ["Yes","No"], submitOnChange: true, width: 6
			input "tableWidth", "number", title: "Table 1 Width (1 - 900)", description: "1-900", required: true, defaultValue: "300", submitOnChange: true
			if(nSections01 == "1") {
				input "secWidth01", "number", title: "Section 1 Percent of Total Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:6
			} else if(nSections01 == "2") {
				input "secWidth01", "number", title: "Section 1 Percent of Total Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth01a", "number", title: "Section 2 Percent of Total Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			} else if(nSections01 == "3") {
				input "secWidth01", "number", title: "Section 1 Percent of Total Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth01a", "number", title: "Section 2 Percent of Total Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth01b", "number", title: "Section 3 Percent of Total Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			}
			
			if(nSections01 == "1") {
				if(secWidth01 == null) {secWidth01 = 100}
				tableLength = secWidth01
			} else if(nSections01 == "2") {
				if(secWidth01 == null) {secWidth01 = 50}
				if(secWidth01a == null) {secWidth01a = 50}
				tableLength = secWidth01 + secWidth01a
			} else if(nSections01 == "3") {
				if(secWidth01 == null) {secWidth01 = 35}
				if(secWidth01a == null) {secWidth01a = 30}
				if(secWidth01b == null) {secWidth01b = 35}
				tableLength = secWidth01 + secWidth01a + secWidth01b
			}
			if(tableLength == 100) {
				paragraph "Table Length: <font color='green'>${tableLength}</font><br><small>* Total table length must equal 100</small>"
			} else {
				paragraph "Table Length: <font color='red'>${tableLength}<br><small>* Total table length must equal 100</small></font>"
			}
		}
		if(nSections01 == "1" || nSections01 == "2" || nSections01 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 01 - Section 1 Options")) {
				paragraph "<b>SECTION 1</b>"
				input "wordsBEF01", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT01", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device01", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device01) {
					def allAtts01 = [:]
					allAtts01 = device01.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts01", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts01
					state.deviceStatus01 = device01.currentValue("${deviceAtts01}")
					if(state.deviceStatus01 == null) state.deviceStatus01 = "No Data"
					if(device01 && deviceAtts01) paragraph "Current Status of Device Attribute: ${device01} - ${deviceAtts01} - ${state.deviceStatus01}"
				}
				input "fontSize01", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align01", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color01", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
				}
		}
		if(nSections01 == "2" || nSections01 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 01 - Section 2 Options")) {
				paragraph "<b>SECTION 2</b>"
				input "wordsBEF01a", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT01a", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device01a", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device01a) {
					def allAtts01a = [:]
					allAtts01a = device01a.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts01a", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts01a
					state.deviceStatus01a = device01a.currentValue("${deviceAtts01a}")
					if(state.deviceStatus01a == null) state.deviceStatus01a = "No Data"
					if(device01a && deviceAtts01a) paragraph "Current Status of Device Attribute: ${device01a} - ${deviceAtts01a} - ${state.deviceStatus01a}"
				}
				input "fontSize01a", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align01a", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color01a", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections01 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 01 - Section 3 Options")) {
				paragraph "<b>SECTION 3</b>"
				input "wordsBEF01b", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT01b", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device01b", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device01b) {
					def allAtts01b = [:]
					allAtts01b = device01b.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts01b", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts01b
					state.deviceStatus01b = device01b.currentValue("${deviceAtts01b}")
					if(state.deviceStatus01b == null) state.deviceStatus01b = "No Data"
					if(device01b && deviceAtts01b) paragraph "Current Status of Device Attribute: ${device01b} - ${deviceAtts01b} - ${state.deviceStatus01b}"
				}
				input "fontSize01b", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align01b", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color01b", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		sampleTileHandler()
	}
}	

def line02Options(){
    dynamicPage(name: "line02Options", title: "Line 02 Options", install: false, uninstall: false){
		section(getFormat("header-green", "${getImage("Blank")}"+" Table Options")) {
			input "nSections02", "enum", title: "Number of Sections", required: true, multiple: false, options: ["1","2","3"], submitOnChange: true, width: 6
			input "border02", "enum", title: "Show a Border for Testing", required: true, multiple: false, options: ["Yes","No"], submitOnChange: true, width: 6
			input "tableWidth", "number", title: "Table 1 Width (1 - 900)", description: "1-900", required: true, defaultValue: "300", submitOnChange: true
			if(nSections02 == "1") {
				input "secWidth02", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:6
			} else if(nSections02 == "2") {
				input "secWidth02", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth02a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			} else if(nSections02 == "3") {
				input "secWidth02", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth02a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth02b", "number", title: "Section 3 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			}
			
			if(nSections02 == "1") {
				if(secWidth02 == null) {secWidth02 = 100}
				tableLength = secWidth02
			} else if(nSections02 == "2") {
				if(secWidth02 == null) {secWidth02 = 50}
				if(secWidth02a == null) {secWidth02a = 50}
				tableLength = secWidth02 + secWidth02a
			} else if(nSections02 == "3") {
				if(secWidth02 == null) {secWidth02 = 35}
				if(secWidth02a == null) {secWidth02a = 30}
				if(secWidth02b == null) {secWidth02b = 35}
				tableLength = secWidth02 + secWidth02a + secWidth02b
			}
			if(tableLength == 100) {
				paragraph "Table Length: <font color='green'>${tableLength}</font><br><small>* Total table length must equal 100</small>"
			} else {
				paragraph "Table Length: <font color='red'>${tableLength}<br><small>* Total table length must equal 100</small></font>"
			}
		}
		if(nSections02 == "1" || nSections02 == "2" || nSections02 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 02 - Section 1 Options")) {
				paragraph "<b>SECTION 1</b>"
				input "wordsBEF02", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT02", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device02", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device02) {
					def allAtts02 = [:]
					allAtts02 = device02.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts02", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts02
					state.deviceStatus02 = device02.currentValue("${deviceAtts02}")
					if(state.deviceStatus02 == null) state.deviceStatus02 = "No Data"
					if(device02 && deviceAtts02) paragraph "Current Status of Device Attribute: ${device02} - ${deviceAtts02} - ${state.deviceStatus02}"
				}
				input "fontSize02", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align02", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color02", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections02 == "2" || nSections02 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 02 - Section 2 Options")) {
				paragraph "<b>SECTION 2</b>"
				input "wordsBEF02a", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT02a", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device02a", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device02a) {
					def allAtts02a = [:]
					allAtts02a = device02a.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts02a", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts02a
					state.deviceStatus02a = device02a.currentValue("${deviceAtts02a}")
					if(state.deviceStatus02a == null) state.deviceStatus02a = "No Data"
					if(device02a && deviceAtts02a) paragraph "Current Status of Device Attribute: ${device02a} - ${deviceAtts02a} - ${state.deviceStatus02a}"
				}
				input "fontSize02a", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align02a", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color02a", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections02 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 02 - Section 3 Options")) {
				paragraph "<b>SECTION 3</b>"
				input "wordsBEF02b", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT02b", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device02b", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device02b) {
					def allAtts02b = [:]
					allAtts02b = device02b.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts02b", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts02b
					state.deviceStatus02b = device02b.currentValue("${deviceAtts02b}")
					if(state.deviceStatus02b == null) state.deviceStatus02b = "No Data"
					if(device02b && deviceAtts02b) paragraph "Current Status of Device Attribute: ${device02b} - ${deviceAtts02b} - ${state.deviceStatus02b}"
				}
				input "fontSize02b", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align02b", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color02b", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		sampleTileHandler()
	}
}	

def line03Options(){
    dynamicPage(name: "line03Options", title: "Line 03 Options", install: false, uninstall: false){
		section(getFormat("header-green", "${getImage("Blank")}"+" Table Options")) {
			input "nSections03", "enum", title: "Number of Sections", required: true, multiple: false, options: ["1","2","3"], submitOnChange: true, width: 6
			input "border03", "enum", title: "Show a Border for Testing", required: true, multiple: false, options: ["Yes","No"], submitOnChange: true, width: 6
			input "tableWidth", "number", title: "Table 1 Width (1 - 900)", description: "1-900", required: true, defaultValue: "300", submitOnChange: true
			if(nSections03 == "1") {
				input "secWidth03", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:6
			} else if(nSections03 == "2") {
				input "secWidth03", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth03a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			} else if(nSections03 == "3") {
				input "secWidth03", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth03a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth03b", "number", title: "Section 3 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			}
			
			if(nSections03 == "1") {
				if(secWidth03 == null) {secWidth03 = 100}
				tableLength = secWidth03
			} else if(nSections03 == "2") {
				if(secWidth03 == null) {secWidth03 = 50}
				if(secWidth03a == null) {secWidth03a = 50}
				tableLength = secWidth03 + secWidth03a
			} else if(nSections03 == "3") {
				if(secWidth03 == null) {secWidth03 = 35}
				if(secWidth03a == null) {secWidth03a = 30}
				if(secWidth03b == null) {secWidth03b = 35}
				tableLength = secWidth03 + secWidth03a + secWidth03b
			}
			if(tableLength == 100) {
				paragraph "Table Length: <font color='green'>${tableLength}</font><br><small>* Total table length must equal 100</small>"
			} else {
				paragraph "Table Length: <font color='red'>${tableLength}<br><small>* Total table length must equal 100</small></font>"
			}
		}
		if(nSections03 == "1" || nSections03 == "2" || nSections03 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 03 - Section 1 Options")) {
				paragraph "<b>SECTION 1</b>"
				input "wordsBEF03", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT03", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device03", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device03) {
					def allAtts03 = [:]
					allAtts03 = device03.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts03", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts03
					state.deviceStatus03 = device03.currentValue("${deviceAtts03}")
					if(state.deviceStatus03 == null) state.deviceStatus03 = "No Data"
					if(device03 && deviceAtts03) paragraph "Current Status of Device Attribute: ${device03} - ${deviceAtts03} - ${state.deviceStatus03}"
				}
				input "fontSize03", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align03", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color03", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections03 == "2" || nSections03 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 03 - Section 2 Options")) {
				paragraph "<b>SECTION 2</b>"
				input "wordsBEF03a", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT03a", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device03a", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device03a) {
					def allAtts03a = [:]
					allAtts03a = device03a.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts03a", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts03a
					state.deviceStatus03a = device03a.currentValue("${deviceAtts03a}")
					if(state.deviceStatus03a == null) state.deviceStatus03a = "No Data"
					if(device03a && deviceAtts03a) paragraph "Current Status of Device Attribute: ${device03a} - ${deviceAtts03a} - ${state.deviceStatus03a}"
				}
				input "fontSize03a", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align03a", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color03a", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections03 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 03 - Section 3 Options")) {
				paragraph "<b>SECTION 3</b>"
				input "wordsBEF03b", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT03b", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device03b", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device03b) {
					def allAtts03b = [:]
					allAtts03b = device03b.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts03b", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts03b
					state.deviceStatus03b = device03b.currentValue("${deviceAtts03b}")
					if(state.deviceStatus03b == null) state.deviceStatus03b = "No Data"
					if(device03b && deviceAtts03b) paragraph "Current Status of Device Attribute: ${device03b} - ${deviceAtts03b} - ${state.deviceStatus03b}"
				}
				input "fontSize03b", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align03b", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color03b", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		sampleTileHandler()
	}
}	

def line04Options(){
    dynamicPage(name: "line04Options", title: "Line 04 Options", install: false, uninstall: false){
		section(getFormat("header-green", "${getImage("Blank")}"+" Table Options")) {
			input "nSections04", "enum", title: "Number of Sections", required: true, multiple: false, options: ["1","2","3"], submitOnChange: true, width: 6
			input "border04", "enum", title: "Show a Border for Testing", required: true, multiple: false, options: ["Yes","No"], submitOnChange: true, width: 6
			input "tableWidth", "number", title: "Table 1 Width (1 - 900)", description: "1-900", required: true, defaultValue: "300", submitOnChange: true
			if(nSections04 == "1") {
				input "secWidth04", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:6
			} else if(nSections04 == "2") {
				input "secWidth04", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth04a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			} else if(nSections04 == "3") {
				input "secWidth04", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth04a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth04b", "number", title: "Section 3 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			}
			
			if(nSections04 == "1") {
				if(secWidth04 == null) {secWidth04 = 100}
				tableLength = secWidth04
			} else if(nSections04 == "2") {
				if(secWidth04 == null) {secWidth04 = 50}
				if(secWidth04a == null) {secWidth04a = 50}
				tableLength = secWidth04 + secWidth04a
			} else if(nSections04 == "3") {
				if(secWidth04 == null) {secWidth04 = 35}
				if(secWidth04a == null) {secWidth04a = 30}
				if(secWidth04b == null) {secWidth04b = 35}
				tableLength = secWidth04 + secWidth04a + secWidth04b
			}
			if(tableLength == 100) {
				paragraph "Table Length: <font color='green'>${tableLength}</font><br><small>* Total table length must equal 100</small>"
			} else {
				paragraph "Table Length: <font color='red'>${tableLength}<br><small>* Total table length must equal 100</small></font>"
			}
		}
		if(nSections04 == "1" || nSections04 == "2" || nSections04 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 04 - Section 1 Options")) {
				paragraph "<b>SECTION 1</b>"
				input "wordsBEF04", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT04", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device04", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device04) {
					def allAtts04 = [:]
					allAtts04 = device04.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts04", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts04
					state.deviceStatus04 = device04.currentValue("${deviceAtts04}")
					if(state.deviceStatus04 == null) state.deviceStatus04 = "No Data"
					if(device04 && deviceAtts04) paragraph "Current Status of Device Attribute: ${device04} - ${deviceAtts04} - ${state.deviceStatus04}"
				}
				input "fontSize04", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align04", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color04", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections04 == "2" || nSections04 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 04 - Section 2 Options")) {
				paragraph "<b>SECTION 2</b>"
				input "wordsBEF04a", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT04a", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device04a", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device04a) {
					def allAtts04a = [:]
					allAtts04a = device04a.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts04a", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts04a
					state.deviceStatus04a = device04a.currentValue("${deviceAtts04a}")
					if(state.deviceStatus04a == null) state.deviceStatus04a = "No Data"
					if(device04a && deviceAtts04a) paragraph "Current Status of Device Attribute: ${device04a} - ${deviceAtts04a} - ${state.deviceStatus04a}"
				}
				input "fontSize04a", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align04a", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color04a", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections04 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 04 - Section 3 Options")) {
				paragraph "<b>SECTION 3</b>"
				input "wordsBEF04b", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT04b", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device04b", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device04b) {
					def allAtts04b = [:]
					allAtts04b = device04b.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts04b", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts04b
					state.deviceStatus04b = device04b.currentValue("${deviceAtts04b}")
					if(state.deviceStatus04b == null) state.deviceStatus04b = "No Data"
					if(device04b && deviceAtts04b) paragraph "Current Status of Device Attribute: ${device04b} - ${deviceAtts04b} - ${state.deviceStatus04b}"
				}
				input "fontSize04b", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align04b", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color04b", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		sampleTileHandler()
	}
}	

def line05Options(){
    dynamicPage(name: "line05Options", title: "Line 05 Options", install: false, uninstall: false){
		section(getFormat("header-green", "${getImage("Blank")}"+" Table Options")) {
			input "nSections05", "enum", title: "Number of Sections", required: true, multiple: false, options: ["1","2","3"], submitOnChange: true, width: 6
			input "border05", "enum", title: "Show a Border for Testing", required: true, multiple: false, options: ["Yes","No"], submitOnChange: true, width: 6
			input "tableWidth", "number", title: "Table 1 Width (1 - 900)", description: "1-900", required: true, defaultValue: "300", submitOnChange: true
			if(nSections05 == "1") {
				input "secWidth05", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:6
			} else if(nSections05 == "2") {
				input "secWidth05", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth05a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			} else if(nSections05 == "3") {
				input "secWidth05", "number", title: "Section 1 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth05a", "number", title: "Section 2 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
				input "secWidth05b", "number", title: "Section 3 Width (1 - 100)", description: "1-100", required: true, submitOnChange: true, width:4
			}
			
			if(nSections05 == "1") {
				if(secWidth05 == null) {secWidth05 = 100}
				tableLength = secWidth05
			} else if(nSections05 == "2") {
				if(secWidth05 == null) {secWidth05 = 50}
				if(secWidth05a == null) {secWidth05a = 50}
				tableLength = secWidth05 + secWidth05a
			} else if(nSections05 == "3") {
				if(secWidth05 == null) {secWidth05 = 35}
				if(secWidth05a == null) {secWidth05a = 30}
				if(secWidth05b == null) {secWidth05b = 35}
				tableLength = secWidth05 + secWidth05a + secWidth05b
			}
			if(tableLength == 100) {
				paragraph "Table Length: <font color='green'>${tableLength}</font><br><small>* Total table length must equal 100</small>"
			} else {
				paragraph "Table Length: <font color='red'>${tableLength}<br><small>* Total table length must equal 100</small></font>"
			}
		}
		if(nSections05 == "1" || nSections05 == "2" || nSections05 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 05 - Section 1 Options")) {
				paragraph "<b>SECTION 1</b>"
				input "wordsBEF05", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT05", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device05", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device05) {
					def allAtts05 = [:]
					allAtts05 = device05.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts05", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts05
					state.deviceStatus05 = device05.currentValue("${deviceAtts05}")
					if(state.deviceStatus05 == null) state.deviceStatus05 = "No Data"
					if(device05 && deviceAtts05) paragraph "Current Status of Device Attribute: ${device05} - ${deviceAtts05} - ${state.deviceStatus05}"
				}
				input "fontSize05", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align05", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color05", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections05 == "2" || nSections05 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 05 - Section 2 Options")) {
				paragraph "<b>SECTION 2</b>"
				input "wordsBEF05a", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT05a", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device05a", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device05a) {
					def allAtts05a = [:]
					allAtts05a = device05a.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts05a", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts05a
					state.deviceStatus05a = device05a.currentValue("${deviceAtts05a}")
					if(state.deviceStatus05a == null) state.deviceStatus05a = "No Data"
					if(device05a && deviceAtts05a) paragraph "Current Status of Device Attribute: ${device05a} - ${deviceAtts05a} - ${state.deviceStatus05a}"
				}
				input "fontSize05a", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align05a", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color05a", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		if(nSections05 == "3") {
			section(getFormat("header-green", "${getImage("Blank")}"+" Line 05 - Section 3 Options")) {
				paragraph "<b>SECTION 3</b>"
				input "wordsBEF05b", "text", title: "Text BEFORE Device Status", required: false, submitOnChange: true, width:6
				input "wordsAFT05b", "text", title: "Text AFTER Device Status", required: false, submitOnChange: true, width:6
				input "device05b", "capability.*", title: "Device", required:false, multiple:false, submitOnChange:true
				if(device05b) {
					def allAtts05b = [:]
					allAtts05b = device05b.supportedAttributes.unique{ it.name }.collectEntries{ [(it):"${it.name.capitalize()}"] }
					input "deviceAtts05b", "enum", title: "Attribute", required:true, multiple:false, submitOnChange:true, options:allAtts05b
					state.deviceStatus05b = device05b.currentValue("${deviceAtts05b}")
					if(state.deviceStatus05b == null) state.deviceStatus05b = "No Data"
					if(device05b && deviceAtts05b) paragraph "Current Status of Device Attribute: ${device05b} - ${deviceAtts05b} - ${state.deviceStatus05b}"
				}
				input "fontSize05b", "number", title: "Font Size", required: true, defaultValue: "15", submitOnChange: true, width:6
				input "align05b", "enum", title: "Alignment", required: true, multiple: false, options: ["Left","Center","Right"], defaultValue: "Left", submitOnChange: true, width: 4
				input "color05b", "text", title: "Text Color (ie. Black, Blue, Brown, Green, Orange, Red, Yellow, White)", required: true, defaultValue: "Black", submitOnChange: true, width:6
			}
		}
		sampleTileHandler()
	}
}	

def installed() {
    log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {	
    if(logEnable) log.debug "Updated with settings: ${settings}"
    unsubscribe()
	unschedule()
	initialize()
}

def initialize() {
    setDefaults()
	if(device01) subscribe(device01, deviceAtts01, sampleTileHandler)
	if(device01a) subscribe(device01a, deviceAtts01a, sampleTileHandler)
	if(device01b) subscribe(device01b, deviceAtts01b, sampleTileHandler)
	if(device02) subscribe(device02, deviceAtts02, sampleTileHandler)
	if(device02a) subscribe(device02a, deviceAtts02a, sampleTileHandler)
	if(device02b) subscribe(device02b, deviceAtts02b, sampleTileHandler)
	if(device03) subscribe(device03, deviceAtts03, sampleTileHandler)
	if(device03a) subscribe(device03a, deviceAtts03a, sampleTileHandler)
	if(device03b) subscribe(device03b, deviceAtts03b, sampleTileHandler)
	if(device04) subscribe(device04, deviceAtts04, sampleTileHandler)
	if(device04a) subscribe(device04a, deviceAtts04a, sampleTileHandler)
	if(device04b) subscribe(device04b, deviceAtts04b, sampleTileHandler)
	if(device05) subscribe(device04, deviceAtts05, sampleTileHandler)
	if(device05a) subscribe(device04a, deviceAtts05a, sampleTileHandler)
	if(device05b) subscribe(device04b, deviceAtts05b, sampleTileHandler)
    
    if(parent.awDevice) schedule("0 0 3 ? * * *", setVersion)
}

def tileHandler01(){
	if(logEnable) log.debug "In tileHandler01..."
	if(state.style01 == null) state.style01 = ""
	if(state.style01a == null) state.style01a = ""
	if(state.style01b == null) state.style01b = ""
	if(words01 == null) words01 = ""
	if(words01a == null) words01a = ""
	if(words01b == null) words01b = ""
	if(nSections01 == "1" || nSections01 == "2" || nSections01 == "3") {
		state.style01 = "text-align:${align01};"
		if(device01) {
			state.deviceStatus01 = device01.currentValue("${deviceAtts01}")
			if(state.deviceStatus01 == null) state.deviceStatus01 = "No Data"
		} else state.deviceStatus01 = ""
	}
	if(nSections01 == "2" || nSections01 == "3") {
		state.style01a = "text-align:${align01a};"
		if(device01a) {
			state.deviceStatus01a = device01a.currentValue("${deviceAtts01a}")
			if(state.deviceStatus01a == null) state.deviceStatus01a = "No Data"
		} else state.deviceStatus01a = ""
	}
	if(nSections01 == "3") {
		state.style01b = "text-align:${align01b};"
		if(device01b) {
			state.deviceStatus01b = device01b.currentValue("${deviceAtts01b}")
			if(state.deviceStatus01b == null) state.deviceStatus01b = "No Data"
		} else state.deviceStatus01b = ""
	}
	
// ***** Make the table for line 1	*****
	
	if(nSections01 == "1") {
		if(border01 == "Yes") {
			state.theTile01 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style01}color:${color01};font-size:${fontSize01}px;width: ${secWidth01}%;'>"
		} else {
			state.theTile01 = "<table width='${tableWidth}'><tr><td style='${state.style01}color:${color01};font-size:${fontSize01}px;width: ${secWidth01}%;'>"
		}
		if(wordsBEF01) state.theTile01 += "${wordsBEF01}"
		if(state.deviceStatus01) state.theTile01 += "${state.deviceStatus01}"
		if(wordsAFT01) state.theTile01 += "${wordsAFT01}"
		
		state.theTile01 += "</td></tr></table>"
		state.theTileLength01 = state.theTile01.length()
	} else if(nSections01 == "2") {
		if(border01 == "Yes") {
			state.theTile01 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style01}color:${color01};font-size:${fontSize01}px;width: ${secWidth01}%;'>"
		} else {
			state.theTile01 = "<table width='${tableWidth}'><tr><td style='${state.style01}color:${color01};font-size:${fontSize01}px;width: ${secWidth01}%;'>"
		}
		if(wordsBEF01) state.theTile01 += "${wordsBEF01}"
		if(state.deviceStatus01) state.theTile01 += "${state.deviceStatus01}"
		if(wordsAFT01) state.theTile01 += "${wordsAFT01}"
		
		state.theTile01 += "</td><td style='${state.style01a}color:${color01a};font-size:${fontSize01a}px;width: ${secWidth01a}%;'>"
		if(wordsBEF01a) state.theTile01 += "${wordsBEF01a}"
		if(state.deviceStatus01a) state.theTile01 += "${state.deviceStatus01a}"
		if(wordsAFT01a) state.theTile01 += "${wordsAFT01a}"
		
		state.theTile01 += "</td></tr></table>"
		state.theTileLength01 = state.theTile01.length()
	} else if(nSections01 == "3") {
		if(border01 == "Yes") {
			state.theTile01 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style01}color:${color01};font-size:${fontSize01}px;width:${secWidth01}%;'>"
		} else {
			state.theTile01 = "<table width='${tableWidth}'><tr><td style='${state.style01}color:${color01};font-size:${fontSize01}px;width:${secWidth01}%;'>"
		}
		if(wordsBEF01) state.theTile01 += "${wordsBEF01}"
		if(state.deviceStatus01) state.theTile01 += "${state.deviceStatus01}"
		if(wordsAFT01) state.theTile01 += "${wordsAFT01}"
		
		state.theTile01 += "</td><td style='${state.style01a}color:${color01a};font-size:${fontSize01a}px;width: ${secWidth01a}%;'>"
		if(wordsBEF01a) state.theTile01 += "${wordsBEF01a}"
		if(state.deviceStatus01a) state.theTile01 += "${state.deviceStatus01a}"
		if(wordsAFT01a) state.theTile01 += "${wordsAFT01a}"
		
		state.theTile01 += "</td><td style='${state.style01b}color:${color01b};font-size:${fontSize01b}px;width: ${secWidth01b}%;'>"
		if(wordsBEF01b) state.theTile01 += "${wordsBEF01b}"
		if(state.deviceStatus01b) state.theTile01 += "${state.deviceStatus01b}"
		if(wordsAFT01b) state.theTile01 += "${wordsAFT01b}"
		
		state.theTile01 += "</td></tr></table>"
		state.theTileLength01 = state.theTile01.length()
	}
	if(logEnable) log.debug "In tileHandler01 - state.theTile01: ${state.theTile01}"
	if(state.theTileLength01 == null) state.theTileLength01 = 0
}

def tileHandler02(){
	if(logEnable) log.debug "In tileHandler02..."
	if(state.style02 == null) state.style02 = ""
	if(state.style02a == null) state.style02a = ""
	if(state.style02b == null) state.style02b = ""
	if(words02 == null) words02 = ""
	if(words02a == null) words02a = ""
	if(words02b == null) words02b = ""
	if(nSections02 == "1" || nSections02 == "2" || nSections02 == "3") {
		state.style02 = "text-align:${align02};"
		if(device02) {
			state.deviceStatus02 = device02.currentValue("${deviceAtts02}")
			if(state.deviceStatus02 == null) state.deviceStatus02 = "No Data"
		} else state.deviceStatus02 = ""
	}
	if(nSections02 == "2" || nSections02 == "3") {
		state.style02a = "text-align:${align02a};"
		if(device02a) {
			state.deviceStatus02a = device02a.currentValue("${deviceAtts02a}")
			if(state.deviceStatus02a == null) state.deviceStatus02a = "No Data"
		} else state.deviceStatus02a = ""
	}
	if(nSections02 == "3") {
		state.style02b = "text-align:${align02b};"
		if(device02b) {
			state.deviceStatus02b = device02b.currentValue("${deviceAtts02b}")
			if(state.deviceStatus02b == null) state.deviceStatus02b = "No Data"
		} else state.deviceStatus02b = ""
	}
	
// ***** Make the table for line 2	*****
	
	if(nSections02 == "1") {
		if(border02 == "Yes") {
			state.theTile02 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style02}color:${color02};font-size:${fontSize02}px;width: ${secWidth02}%;'>"
		} else {
			state.theTile02 = "<table width='${tableWidth}'><tr><td style='${state.style02}color:${color02};font-size:${fontSize02}px;width: ${secWidth02}%;'>"
		}
		if(wordsBEF02) state.theTile02 += "${wordsBEF02}"
		if(state.deviceStatus02) state.theTile02 += "${state.deviceStatus02}"
		if(wordsAFT02) state.theTile02 += "${wordsAFT02}"
		
		state.theTile02 += "</td></tr></table>"
		state.theTileLength02 = state.theTile02.length()
	} else if(nSections02 == "2") {
		if(border02 == "Yes") {
			state.theTile02 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style02}color:${color02};font-size:${fontSize02}px;width:${secWidth02}%;'>"
		} else {
			state.theTile02 = "<table width='${tableWidth}'><tr><td style='${state.style02}color:${color02};font-size:${fontSize02}px;width:${secWidth02}%;'>"
		}
		if(wordsBEF02) state.theTile02 += "${wordsBEF02}"
		if(state.deviceStatus02) state.theTile02 += "${state.deviceStatus02}"
		if(wordsAFT02) state.theTile02 += "${wordsAFT02}"
		
		state.theTile02 += "</td><td style='${state.style02a}color:${color02a};font-size:${fontSize02a}px;width: ${secWidth02a}%;'>"
		if(wordsBEF02a) state.theTile02 += "${wordsBEF02a}"
		if(state.deviceStatus02a) state.theTile02 += "${state.deviceStatus02a}"
		if(wordsAFT02a) state.theTile02 += "${wordsAFT02a}"
		
		state.theTile02 += "</td></tr></table>"
		state.theTileLength02 = state.theTile02.length()
	} else if(nSections02 == "3") {
		if(border02 == "Yes") {
			state.theTile02 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style02}color:${color02};font-size:${fontSize02}px;width: ${secWidth02}%;'>"
		} else {
			state.theTile02 = "<table width='${tableWidth}'><tr><td style='${state.style02}color:${color02};font-size:${fontSize02}px;width: ${secWidth02}%;'>"
		}
		if(wordsBEF02) state.theTile02 += "${wordsBEF02}"
		if(state.deviceStatus02) state.theTile02 += "${state.deviceStatus02}"
		if(wordsAFT02) state.theTile02 += "${wordsAFT02}"
		
		state.theTile02 += "</td><td style='${state.style02a}color:${color02a};font-size:${fontSize02a}px;width: ${secWidth02a}%;'>"
		if(wordsBEF02a) state.theTile02 += "${wordsBEF02a}"
		if(state.deviceStatus02a) state.theTile02 += "${state.deviceStatus02a}"
		if(wordsAFT02a) state.theTile02 += "${wordsAFT02a}"
		
		state.theTile02 += "</td><td style='${state.style02b}color:${color02b};font-size:${fontSize02b}px;width: ${secWidth02b}%;'>"
		if(wordsBEF02b) state.theTile02 += "${wordsBEF02b}"
		if(state.deviceStatus02b) state.theTile02 += "${state.deviceStatus02b}"
		if(wordsAFT02b) state.theTile02 += "${wordsAFT02b}"
		
		state.theTile02 += "</td></tr></table>"
		state.theTileLength02 = state.theTile02.length()
	}
	if(logEnable) log.debug "In tileHandler02 - state.theTile02: ${state.theTile02}"
	if(state.theTileLength02 == null) state.theTileLength02 = 0
}

def tileHandler03(){
	if(logEnable) log.debug "In tileHandler03..."
	if(state.style03 == null) state.style03 = ""
	if(state.style03a == null) state.style03a = ""
	if(state.style03b == null) state.style03b = ""
	if(words03 == null) words03 = ""
	if(words03a == null) words03a = ""
	if(words03b == null) words03b = ""
	if(nSections03 == "1" || nSections03 == "2" || nSections03 == "3") {
		state.style03 = "text-align:${align03};"
		if(line03) state.style03 += "text-decoration:${line03};"
		if(bold03) state.style03 += "font-weight:bold;"
		if(italic03) state.style03 += "font-style:italic;"
		if(device03) {
			state.deviceStatus03 = device03.currentValue("${deviceAtts03}")
			if(state.deviceStatus03 == null) state.deviceStatus03 = "No Data"
		} else state.deviceStatus03 = ""
	}
	if(nSections03 == "2" || nSections03 == "3") {
		state.style03a = "text-align:${align03a};"
		if(device03a) {
			state.deviceStatus03a = device03a.currentValue("${deviceAtts03a}")
			if(state.deviceStatus03a == null) state.deviceStatus03a = "No Data"
		} else state.deviceStatus03a = ""
	}
	if(nSections03 == "3") {
		state.style03b = "text-align:${align03b};"
		if(device03b) {
			state.deviceStatus03b = device03b.currentValue("${deviceAtts03b}")
			if(state.deviceStatus03b == null) state.deviceStatus03b = "No Data"
		} else state.deviceStatus03b = ""
	}
	
// ***** Make the table for line 3	*****
	
	if(nSections03 == "1") {
		if(border03 == "Yes") {
			state.theTile03 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style03}color:${color03};font-size:${fontSize03}px;width: ${secWidth03}%;'>"
		} else {
			state.theTile03 = "<table width='${tableWidth}'><tr><td style='${state.style03}color:${color03};font-size:${fontSize03}px;width: ${secWidth03}%;'>"
		}
		if(wordsBEF03) state.theTile03 += "${wordsBEF03}"
		if(state.deviceStatus03) state.theTile03 += "${state.deviceStatus03}"
		if(wordsAFT03) state.theTile03 += "${wordsAFT03}"
		
		state.theTile03 += "</td></tr></table>"
		state.theTileLength03 = state.theTile03.length()
	} else if(nSections03 == "2") {
		if(border03 == "Yes") {
			state.theTile03 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style03}color:${color03};font-size:${fontSize03}px;width:${secWidth03}%;'>"
		} else {
			state.theTile03 = "<table width='${tableWidth}'><tr><td style='${state.style03}color:${color03};font-size:${fontSize03}px;width:${secWidth03}%;'>"
		}
		if(wordsBEF03) state.theTile03 += "${wordsBEF03}"
		if(state.deviceStatus03) state.theTile03 += "${state.deviceStatus03}"
		if(wordsAFT03) state.theTile03 += "${wordsAFT03}"
		
		state.theTile03 += "</td><td style='${state.style03a}color:${color03a};font-size:${fontSize03a}px;width: ${secWidth03a}%;'>"
		if(wordsBEF03a) state.theTile03 += "${wordsBEF03a}"
		if(state.deviceStatus03a) state.theTile03 += "${state.deviceStatus03a}"
		if(wordsAFT03a) state.theTile03 += "${wordsAFT03a}"
		
		state.theTile03 += "</td></tr></table>"
		state.theTileLength03 = state.theTile03.length()
	} else if(nSections03 == "3") {
		if(border03 == "Yes") {
			state.theTile03 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style03}color:${color03};font-size:${fontSize03}px;width: ${secWidth03}%;'>"
		} else {
			state.theTile03 = "<table width='${tableWidth}'><tr><td style='${state.style03}color:${color03};font-size:${fontSize03}px;width: ${secWidth03}%;'>"
		}
		if(wordsBEF03) state.theTile03 += "${wordsBEF03}"
		if(state.deviceStatus03) state.theTile03 += "${state.deviceStatus03}"
		if(wordsAFT03) state.theTile03 += "${wordsAFT03}"
		
		state.theTile03 += "</td><td style='${state.style03a}color:${color03a};font-size:${fontSize03a}px;width: ${secWidth03a}%;'>"
		if(wordsBEF03a) state.theTile03 += "${wordsBEF03a}"
		if(state.deviceStatus03a) state.theTile03 += "${state.deviceStatus03a}"
		if(wordsAFT03a) state.theTile03 += "${wordsAFT03a}"
		
		state.theTile03 += "</td><td style='${state.style03b}color:${color03b};font-size:${fontSize03b}px;width: ${secWidth03b}%;'>"
		if(wordsBEF03b) state.theTile03 += "${wordsBEF03b}"
		if(state.deviceStatus03b) state.theTile03 += "${state.deviceStatus03b}"
		if(wordsAFT03b) state.theTile03 += "${wordsAFT03b}"
		
		state.theTile03 += "</td></tr></table>"
		state.theTileLength03 = state.theTile03.length()
	}
	if(logEnable) log.debug "In tileHandler03 - state.theTile03: ${state.theTile03}"
	if(state.theTileLength03 == null) state.theTileLength03 = 0
}

def tileHandler04(){
	if(logEnable) log.debug "In tileHandler04..."
	if(state.style04 == null) state.style04 = ""
	if(state.style04a == null) state.style04a = ""
	if(state.style04b == null) state.style04b = ""
	if(words04 == null) words04 = ""
	if(words04a == null) words04a = ""
	if(words04b == null) words04b = ""
	if(nSections04 == "1" || nSections04 == "2" || nSections04 == "3") {
		state.style04 = "text-align:${align04};"
		if(line04) state.style04 += "text-decoration:${line04};"
		if(bold04) state.style04 += "font-weight:bold;"
		if(italic04) state.style04 += "font-style:italic;"
		if(device04) {
			state.deviceStatus04 = device04.currentValue("${deviceAtts04}")
			if(state.deviceStatus04 == null) state.deviceStatus04 = "No Data"
		} else state.deviceStatus04 = ""
	}
	if(nSections04 == "2" || nSections04 == "3") {
		state.style04a = "text-align:${align04a};"
		if(device04a) {
			state.deviceStatus04a = device04a.currentValue("${deviceAtts04a}")
			if(state.deviceStatus04a == null) state.deviceStatus04a = "No Data"
		} else state.deviceStatus04a = ""
	}
	if(nSections04 == "3") {
		state.style04b = "text-align:${align04b};"
		if(device04b) {
			state.deviceStatus04b = device04b.currentValue("${deviceAtts04b}")
			if(state.deviceStatus04b == null) state.deviceStatus04b = "No Data"
		} else state.deviceStatus04b = ""
	}
	
// ***** Make the table for line 4	*****
	
	if(nSections04 == "1") {
		if(border04 == "Yes") {
			state.theTile04 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style04}color:${color04};font-size:${fontSize04}px;width: ${secWidth04}%;'>"
		} else {
			state.theTile04 = "<table width='${tableWidth}'><tr><td style='${state.style04}color:${color04};font-size:${fontSize04}px;width:${secWidth04}%;'>"	// 61 + 12 + 17 (100)
		}
		if(wordsBEF04) state.theTile04 += "${wordsBEF04}"
		if(state.deviceStatus04) state.theTile04 += "${state.deviceStatus04}"
		if(wordsAFT04) state.theTile04 += "${wordsAFT04}"
		
		state.theTile04 += "</td></tr></table>"		// 18
		state.theTileLength04 = state.theTile04.length()
	} else if(nSections04 == "2") {
		if(border04 == "Yes") {
			state.theTile04 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style04}color:${color04};font-size:${fontSize04}px;width:${secWidth04}%;'>"
		} else {
			state.theTile04 = "<table width='${tableWidth}'><tr><td style='${state.style04}color:${color04};font-size:${fontSize04}px;width:${secWidth04}%;'>"
		}
		if(wordsBEF04) state.theTile04 += "${wordsBEF04}"
		if(state.deviceStatus04) state.theTile04 += "${state.deviceStatus04}"
		if(wordsAFT04) state.theTile04 += "${wordsAFT04}"
		
		state.theTile04 += "</td><td style='${state.style04a}color:${color04a};font-size:${fontSize04a}px;width: ${secWidth04a}%;'>"
		if(wordsBEF04a) state.theTile04 += "${wordsBEF04a}"
		if(state.deviceStatus04a) state.theTile04 += "${state.deviceStatus04a}"
		if(wordsAFT04a) state.theTile04 += "${wordsAFT04a}"
		
		state.theTile04 += "</td></tr></table>"
		state.theTileLength04 = state.theTile04.length()
	} else if(nSections04 == "3") {
		if(border04 == "Yes") {
			state.theTile04 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style04}color:${color04};font-size:${fontSize04}px;width: ${secWidth04}%;'>"
		} else {
			state.theTile04 = "<table width='${tableWidth}'><tr><td style='${state.style04}color:${color04};font-size:${fontSize04}px;width: ${secWidth04}%;'>"
		}
		if(wordsBEF04) state.theTile04 += "${wordsBEF04}"
		if(state.deviceStatus04) state.theTile04 += "${state.deviceStatus04}"
		if(wordsAFT04) state.theTile04 += "${wordsAFT04}"
		
		state.theTile04 += "</td><td style='${state.style04a}color:${color04a};font-size:${fontSize04a}px;width: ${secWidth04a}%;'>"
		if(wordsBEF04a) state.theTile04 += "${wordsBEF04a}"
		if(state.deviceStatus04a) state.theTile04 += "${state.deviceStatus04a}"
		if(wordsAFT04a) state.theTile04 += "${wordsAFT04a}"
		
		state.theTile04 += "</td><td style='${state.style04b}color:${color04b};font-size:${fontSize04b}px;width: ${secWidth04b}%;'>"
		if(wordsBEF04b) state.theTile04 += "${wordsBEF04b}"
		if(state.deviceStatus04b) state.theTile04 += "${state.deviceStatus04b}"
		if(wordsAFT04b) state.theTile04 += "${wordsAFT04b}"
		
		state.theTile04 += "</td></tr></table>"
		state.theTileLength04 = state.theTile04.length()
	}
	if(logEnable) log.debug "In tileHandler04 - state.theTile04: ${state.theTile04}"
	if(state.theTileLength04 == null) state.theTileLength04 = 0
}

def tileHandler05(){
	if(logEnable) log.debug "In tileHandler05..."
	if(state.style05 == null) state.style05 = ""
	if(state.style05a == null) state.style05a = ""
	if(state.style05b == null) state.style05b = ""
	if(words05 == null) words05 = ""
	if(words05a == null) words05a = ""
	if(words05b == null) words05b = ""
	if(nSections05 == "1" || nSections05 == "2" || nSections05 == "3") {
		state.style05 = "text-align:${align05};"
		if(line05) state.style05 += "text-decoration:${line05};"
		if(bold05) state.style05 += "font-weight:bold;"
		if(italic05) state.style05 += "font-style:italic;"
		if(device05) {
			state.deviceStatus05 = device05.currentValue("${deviceAtts05}")
			if(state.deviceStatus05 == null) state.deviceStatus05 = "No Data"
		} else state.deviceStatus05 = ""
	}
	if(nSections05 == "2" || nSections05 == "3") {
		state.style05a = "text-align:${align05a};"
		if(device05a) {
			state.deviceStatus05a = device05a.currentValue("${deviceAtts05a}")
			if(state.deviceStatus05a == null) state.deviceStatus05a = "No Data"
		} else state.deviceStatus05a = ""
	}
	if(nSections05 == "3") {
		state.style05b = "text-align:${align05b};"
		if(device05b) {
			state.deviceStatus05b = device05b.currentValue("${deviceAtts05b}")
			if(state.deviceStatus05b == null) state.deviceStatus05b = "No Data"
		} else state.deviceStatus05b = ""
	}
	
// ***** Make the table for line 5	*****
	
	if(nSections05 == "1") {
		if(border05 == "Yes") {
			state.theTile05 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style05}color:${color05};font-size:${fontSize05}px;width: ${secWidth05}%;'>"
		} else {
			state.theTile05 = "<table width='${tableWidth}'><tr><td style='${state.style05}color:${color05};font-size:${fontSize05}px;width: ${secWidth05}%;'>"
		}
		if(wordsBEF05) state.theTile05 += "${wordsBEF05}"
		if(state.deviceStatus05) state.theTile05 += "${state.deviceStatus05}"
		if(wordsAFT05) state.theTile05 += "${wordsAFT05}"
		
		state.theTile05 += "</td></tr></table>"
		state.theTileLength05 = state.theTile05.length()
	} else if(nSections05 == "2") {
		if(border05 == "Yes") {
			state.theTile05 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style05}color:${color05};font-size:${fontSize05}px;width:${secWidth05}%;'>"
		} else {
			state.theTile05 = "<table width='${tableWidth}'><tr><td style='${state.style05}color:${color05};font-size:${fontSize05}px;width:${secWidth05}%;'>"
		}
		if(wordsBEF05) state.theTile05 += "${wordsBEF05}"
		if(state.deviceStatus05) state.theTile05 += "${state.deviceStatus05}"
		if(wordsAFT05) state.theTile05 += "${wordsAFT05}"
		
		state.theTile05 += "</td><td style='${state.style05a}color:${color05a};font-size:${fontSize05a}px;width: ${secWidth05a}%;'>"
		if(wordsBEF05a) state.theTile05 += "${wordsBEF05a}"
		if(state.deviceStatus05a) state.theTile05 += "${state.deviceStatus05a}"
		if(wordsAFT05a) state.theTile05 += "${wordsAFT05a}"
		
		state.theTile05 += "</td></tr></table>"
		state.theTileLength05 = state.theTile05.length()
	} else if(nSections05 == "3") {
		if(border05 == "Yes") {
			state.theTile05 = "<table border=1 width='${tableWidth}'><tr><td style='${state.style05}color:${color05};font-size:${fontSize05}px;width: ${secWidth05}%;'>"
		} else {
			state.theTile05 = "<table width='${tableWidth}'><tr><td style='${state.style05}color:${color05};font-size:${fontSize05}px;width: ${secWidth05}%;'>"
		}
		if(wordsBEF05) state.theTile05 += "${wordsBEF05}"
		if(state.deviceStatus05) state.theTile05 += "${state.deviceStatus05}"
		if(wordsAFT05) state.theTile05 += "${wordsAFT05}"
		
		state.theTile05 += "</td><td style='${state.style05a}color:${color05a};font-size:${fontSize05a}px;width: ${secWidth05a}%;'>"
		if(wordsBEF05a) state.theTile05 += "${wordsBEF05a}"
		if(state.deviceStatus05a) state.theTile05 += "${state.deviceStatus05a}"
		if(wordsAFT05a) state.theTile05 += "${wordsAFT05a}"
		
		state.theTile05 += "</td><td style='${state.style05b}color:${color05b};font-size:${fontSize05b}px;width: ${secWidth05b}%;'>"
		if(wordsBEF05b) state.theTile05 += "${wordsBEF05b}"
		if(state.deviceStatus05b) state.theTile05 += "${state.deviceStatus05b}"
		if(wordsAFT05b) state.theTile05 += "${wordsAFT05b}"
		
		state.theTile05 += "</td></tr></table>"
		state.theTileLength05 = state.theTile05.length()
	}
	if(logEnable) log.debug "In tileHandler05 - state.theTile05: ${state.theTile05}"
	if(state.theTileLength05 == null) state.theTileLength05 = 0
}

def sampleTileHandler(evt){
	if(logEnable) log.debug "In sampleTileHandler"
	tileHandler01()
	tileHandler02()
	tileHandler03()
	tileHandler04()
	tileHandler05()
	
	if(logEnable) log.debug "In sampleTileHandler - Back in sampleTileHandler"
	section(getFormat("header-green", "${getImage("Blank")}"+" Sample Tile")) {
		if(state.theTile01 && !state.theTile02) {
			paragraph "<table align='center'><tr><td>${state.theTile01}</td></tr></table>"
		}
		if(state.theTile01 && state.theTile02 && !state.theTile03) {
			paragraph "<table align='center'><tr><td>${state.theTile01}${state.theTile02}</td></tr></table>"
		}
		if(state.theTile01 && state.theTile02 && state.theTile03 && !state.theTile04) {
			paragraph "<table align='center'><tr><td>${state.theTile01}${state.theTile02}${state.theTile03}</td></tr></table>"
		}
		if(state.theTile01 && state.theTile02 && state.theTile03 && state.theTile04 && !state.theTile05) {
			paragraph "<table align='center'><tr><td>${state.theTile01}${state.theTile02}${state.theTile03}${state.theTile04}</td></tr></table>"
		}
		if(state.theTile01 && state.theTile02 && state.theTile03 && state.theTile04 && state.theTile05) {
			paragraph "<table align='center'><tr><td>${state.theTile01}${state.theTile02}${state.theTile03}${state.theTile04}${state.theTile05}</td></tr></table>"
		}
		
		if(logEnable) log.debug "In sampleTileHandler - theTileLength01: ${state.theTileLength01} - tTL02: ${state.theTileLength02} - tTL03: ${state.theTileLength03} - tTL04: ${state.theTileLength04} - tTL05: ${state.theTileLength05}"
		int tileLength01 = state.theTileLength01 + 20
		int tileLength02 = state.theTileLength02 + 20
		int tileLength03 = state.theTileLength03 + 20
		int tileLength04 = state.theTileLength04 + 20
		int tileLength05 = state.theTileLength05 + 20
		
		if(logEnable) log.debug "In sampleTileHandler - tileLength01: ${tileLength01} - tL02: ${tileLength02} - tL03: ${tileLength03} - tL04: ${tileLength04} - tL05: ${tileLength05}"
		
		int totalLength = tileLength01 + tileLength02 + tileLength03 + tileLength04 + tileLength05
        paragraph "<hr>"
		paragraph "Characters - Line 1: ${tileLength01} - Line 2: ${tileLength02} - Line 3: ${tileLength03} - Line 4: ${tileLength04} - Line 5: ${tileLength05}<br>* This is only an estimate. Actual character count can be found in the tile device."
		if(totalLength <= 1024) {
			paragraph "Total Number of Characters: <font color='green'>${totalLength}</font><br><small>* Must stay under 1024 to display on Dashboard.</small>"
		} else {
			paragraph "Total Number of Characters: <font color='red'>${totalLength}<br><small>* Must stay under 1024 to display on Dashboard.</small></font>"
		}
        makeTile()
	}
}

def makeTile() {
    if(logEnable) log.debug "In makeTile"
    tileData = "<table align='center'>"  // 21
    if(state.theTile01) tileData +="<tr><td>${state.theTile01}</td></tr>"    // 18
    if(state.theTile02) tileData +="<tr><td>${state.theTile02}</td></tr>"
    if(state.theTile03) tileData +="<tr><td>${state.theTile03}</td></tr>"
    if(state.theTile04) tileData +="<tr><td>${state.theTile04}</td></tr>"
    if(state.theTile05) tileData +="<tr><td>${state.theTile05}</td></tr>"
    tileData +="</table>"    // 8
    if(logEnable) log.debug "${tileData}"
    if(tileDevice) {
        tileDevice.sendTile01(tileData)
        if(logEnable) log.debug "In makeTile - tileData sent"
    }
}

// ********** Normal Stuff **********

def setDefaults(){
	if(logEnable == null){logEnable = false}
	
	if(state.tile1 == null) {state.tile1 = ""}
	if(state.tile2 == null) {state.tile2 = ""}
	if(state.tile3 == null) {state.tile3 = ""}
	if(state.tile4 == null) {state.tile4 = ""}
	if(state.tile5 == null) {state.tile5 = ""}
	if(state.theTileLength01 == null) {state.theTileLength01 = 0}
	if(state.theTileLength02 == null) {state.theTileLength02 = 0}
	if(state.theTileLength03 == null) {state.theTileLength03 = 0}
	if(state.theTileLength04 == null) {state.theTileLength04 = 0}
	if(state.theTileLength05 == null) {state.theTileLength05 = 0}
}

def getImage(type) {					// Modified from @Stephack Code
    def loc = "<img src=https://raw.githubusercontent.com/bptworld/Hubitat/master/resources/images/"
    if(type == "Blank") return "${loc}blank.png height=40 width=5}>"
}

def getFormat(type, myText=""){			// Modified from @Stephack Code
	if(type == "header-green") return "<div style='color:#ffffff;font-weight: bold;background-color:#81BC00;border: 1px solid;box-shadow: 2px 3px #A9A9A9'>${myText}</div>"
    if(type == "line") return "\n<hr style='background-color:#1A77C9; height: 1px; border: 0;'></hr>"
	if(type == "title") return "<div style='color:blue;font-weight: bold'>${myText}</div>"
}

def display() {
	section() {
		paragraph getFormat("line")
	}
}

def display2(){
	setVersion()
	section() {
		paragraph getFormat("line")
		paragraph "<div style='color:#1A77C9;text-align:center'>Tile Master - @BPTWorld<br><a href='https://github.com/bptworld/Hubitat' target='_blank'>Find more apps on my Github, just click here!</a><br>Get app update notifications and more with <a href='https://github.com/bptworld/Hubitat/tree/master/Apps/App%20Watchdog' target='_blank'>App Watchdog</a><br>${state.version}</div>"
	}       
}
