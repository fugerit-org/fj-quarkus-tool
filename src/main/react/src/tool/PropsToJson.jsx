import React, { Component, Fragment } from 'react';
import { FormControl, Select, MenuItem, TextField, Grid, FormLabel, Button } from "@mui/material";
import appService from '../common/app-service';
import DOMPurify from 'isomorphic-dompurify';

import AceEditor from "react-ace";

import MarkdownEditor from '@uiw/react-markdown-editor';

import "ace-builds/src-noconflict/mode-xml";
import "ace-builds/src-noconflict/mode-properties";
import "ace-builds/src-noconflict/mode-kotlin";
import "ace-builds/src-noconflict/mode-ftl";
import "ace-builds/src-noconflict/mode-markdown";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";

class PropsToJson extends Component {

	constructor(props) {
		super(props);
		this.handleGenerate = this.handleGenerate.bind(this);
		this.handleValidate = this.handleValidate.bind(this);
		this.handleFormat = this.handleFormat.bind(this);
		this.handleDoc = this.handleDoc.bind(this);
		this.handleFreemarkerData = this.handleFreemarkerData.bind(this);
		this.handleEditorContent = this.handleEditorContent.bind(this);
		this.handleJsonDataContent = this.handleJsonDataContent.bind(this);
		this.handleInputFormat = this.handleInputFormat.bind(this);
		this.state = {
			renderCatalog: true,
			inputFormat: 'PROPERTIES',
			outputFormat: 'JSON1',
			docContent: '',
			freemarkerJsonData: '{"docTitle":"My FreeMarker Template Sample Doc Title"}',
			docOutput: null,
			docFormat: null,
			generationTime: null,
			outputMessage: null,
			validationEnabled: false,
			info: null
		}
	}

	handleGenerate = (e) => {
		e.preventDefault();
		if (this.state.outputFormat == null) {
			this.props.handleOpenDialog("Select an output format");
		} else {
			let reactState = this
			let payload = {}
			payload.inputFormat = this.state.inputFormat;
			payload.outputFormat = this.state.outputFormat;
			payload.docContent = this.state.docContent;
			payload.freemarkerJsonData = this.state.freemarkerJsonData;
			appService.doAjaxJson('POST', '/props_to_json/convert', payload).then(response => {
				if (response.success) {
					reactState.setState({
						docOutput: response.result.docOutput,
						docFormat: this.state.outputFormat,
						generationTime:  response.result.generationTime,
						outputMessage: response.result.message,
						info: response.result.info
					})
				} 
			})
		}
	};
	
	handleValidate = (e) => {
		e.preventDefault();
		if (this.state.outputFormat == null) {
			this.props.handleOpenDialog("Select an output format");
		} else {
			let reactState = this
			let payload = {}
			payload.inputFormat = this.state.inputFormat;
			payload.outputFormat = this.state.outputFormat;
			payload.docContent = this.state.docContent;
			appService.doAjaxJson('POST', '/generate/validate', payload).then(response => {
				if (response.success) {
					reactState.setState({
						docOutput: response.result.docOutputBase64,
						docFormat: this.state.outputFormat,
						outputMessage: response.result.message
					})
				} 
			})
		}
	};		
	
	handleInputFormat = (e) => {
		e.preventDefault();
		this.setState({
			renderCatalog : true,
			inputFormat: e.target.value,
			validationEnabled: (e.target.value !== 'FTLX')
		});
	};

	handleFormat = (e) => {
		e.preventDefault();
		this.setState({
			outputFormat: e.target.value,
			docContent: this.state.docContent
		});
	};

	handleDoc( newValue ) {
		this.setState({
			outputFormat: this.state.outputFormat,
			docContent: newValue
		}); 
	};
	
	handleFreemarkerData( newValue ) {
		this.setState({
			outputFormat: this.state.outputFormat,
			freemarkerJsonData: newValue
		}); 
	};
	
	handleEditorContent = ( content ) => {
		this.setState(
			{ 
				docContent: content			 
			}
		);
	};

	handleJsonDataContent = ( content ) => {
		this.setState(
			{ 
				freemarkerJsonData: content			 
			}
		);
	};

	render() {
		
		let freemarkerJsonData = '';
		let editorInFormat = 'xml';
		if ( this.state.inputFormat != null ) {
			editorInFormat = this.state.inputFormat.toLowerCase()
		}

		let outputData = <Fragment>Here will be the output</Fragment>
		if ( this.state.docOutput != null && this.state.docFormat != null ) {
			outputData = <AceEditor
				mode='json'
				theme="xcode"
				name="DOC_XML_OUTPUT"
				editorProps={{ $blockScrolling: true }}
				enableBasicAutocompletion={true}
				enableLiveAutocompletion={true}
				enableSnippets={true}
				value={this.state.docOutput}
				width='100%'
			/>
			outputData = <Fragment>{outputData}<p>Generation time : {this.state.generationTime}</p></Fragment>
		} else if ( this.state.outputMessage != null ) {
			outputData =  <Grid id="firstRow" container spacing={1} style={{ paddingLeft: 5, paddingTop: 15 }}>
 				<Grid item sx={{ width: '100%' }}>
 					<TextField sx={{ width: '100%' }} 
 						id="outputMessage" 
 						rows={20}
 						multiline={true}
 						label="output message" 
 						variant="outlined" value={this.state.outputMessage}/>
 				</Grid>
 			</Grid>
		}
		
		return <Fragment>

			<Grid container spacing={1}>
			  <Grid item xs={6}>
			    <Button variant="contained" onClick={this.handleGenerate}>Convert</Button>
			  </Grid>
			  <Grid item xs={6}>

			  </Grid>
			  <Grid item xs={12} xl={6}>
						<AceEditor
							mode={editorInFormat}
							theme="xcode"
							name="DOC_ACE_EDITOR"
							editorProps={{ $blockScrolling: true }}
							enableBasicAutocompletion={true}
							enableLiveAutocompletion={true}
							enableSnippets={true}
							value={this.state.docContent}
							onChange={this.handleDoc}
							width='100%'
						/>
					{freemarkerJsonData}
			  </Grid>
			  <Grid item xs={12} xl={6}>
			    {outputData}
			  </Grid>
				<Grid item xs={12} xl={12}>
					Additional info :
				</Grid>
			  <Grid item xs={12} xl={12}>
				  <AceEditor
					  mode='txt'
					  theme="xcode"
					  name="DOC_ACE_EDITOR"
					  editorProps={{ $blockScrolling: true }}
					  enableBasicAutocompletion={true}
					  enableLiveAutocompletion={true}
					  enableSnippets={true}
					  value={this.state.info}
					  width='100%'
				  />
			  </Grid>
			</Grid>
			
		</Fragment>
	}

}

export default PropsToJson;