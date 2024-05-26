import React, { Component, Fragment } from 'react';
import { Input, Grid, FormLabel, Button, TextField } from "@mui/material";
import appService from '../common/app-service';

import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-properties";

class ExcelToProperties extends Component {

	constructor(props) {
		super(props);
		this.sendForValidation = this.sendForValidation.bind(this);
		this.prepareSend = this.prepareSend.bind(this);
		this.state = {
			fileToValidate: null,
			supportedExtensions: null,
			validationResult: null,
			sheetIndex: 0,
			keyColumnIndex : 0,
			valueColumnIndex : 0,
			skipHeaderLines: 1,
			docOutput: ''
		}
	}

	sendForValidation = (e) => {
		e.preventDefault();
		if (this.state.fileToValidate == null) {
			alert('File non provided');
		} else {
			let formData = new FormData();
			formData.append('file', this.state.fileToValidate);
			let reactState = this;
			appService.doAjaxMultipart('POST', '/excel_to_props/convert/'+this.state.sheetIndex+'/'+this.state.keyColumnIndex+'/'+this.state.valueColumnIndex+'/'+this.state.skipHeaderLines, formData).then(response => {
				if (response.success) {
					reactState.setState({
						validationResult: response.result?.message,
						docOutput: response.result?.docOutput
					})
				} else {
					reactState.setState({
						validationResult: response.status
					})
				}
			})
		}
	};

	prepareSend = (e) => {
		e.preventDefault();
		this.setState({
			fileToValidate: e.target.files[0]
		})
	};

	handleSheetIndex = (e) => {
		e.preventDefault();
		this.setState({
			sheetIndex: e.target.value
		})
	};

	handleKeyColumnIndex = (e) => {
		e.preventDefault();
		this.setState({
			keyColumnIndex: e.target.value
		})
	};

	handleValueColumnIndex = (e) => {
		e.preventDefault();
		this.setState({
			valueColumnIndex: e.target.value
		})
	};

	handleSkipHeaderLines = (e) => {
		e.preventDefault();
		this.setState({
			skipHeaderLines: e.target.value
		})
	};

	render() {
		let message = <Fragment></Fragment>
		if (this.state.validationResult != null) {
			message = <b>Validation result : {this.state.validationResult} </b>
		}

		return (
			<Fragment>
				<Grid container spacing={1}>
				  <Grid item xs={8}>
				  	<Grid container spacing={1}>
				  		<Grid item xs={12}>
				  			{message}
				  		</Grid>
				  		<Grid item xs={12}>
				  			<FormLabel>Convert excel to properties:</FormLabel>
				  		</Grid>
						<Grid item xs={6}>
							<TextField fullWidth={true} id="sheetIndex" label="Sheet index (starts with 0)" variant="outlined" value={this.state.sheetIndex} onChange={this.handleSheetIndex}/>
						</Grid>
						<Grid item xs={6}>
							<TextField fullWidth={true} id="keyColumnIndex" label="Key column index (starts with 0)" variant="outlined" value={this.state.keyColumnIndex} onChange={this.handleKeyColumnIndex}/>
						</Grid>
						<Grid item xs={6}>
							<TextField fullWidth={true} id="valueColumnIndex" label="Value column index (starts with 0)" variant="outlined" value={this.state.valueColumnIndex} onChange={this.handleValueColumnIndex}/>
						</Grid>
						<Grid item xs={6}>
							<TextField fullWidth={true} id="skipHeaderLines" label="Skip number of header lines" variant="outlined" value={this.state.skipHeaderLines} onChange={this.handleSkipHeaderLines}/>
						</Grid>
				  		<Grid item xs={12}>
				  			<Input type='file' onChange={this.prepareSend}/>
				  		</Grid>
				  		<Grid item xs={12}>
				  			<Button variant="contained" component="label" onClick={this.sendForValidation}>Send</Button>
				  		</Grid>
						<Grid item xs={12}>
							<AceEditor
								mode='properties'
								theme="xcode"
								name="DOC_ACE_EDITOR"
								editorProps={{ $blockScrolling: true }}
								enableBasicAutocompletion={true}
								enableLiveAutocompletion={true}
								enableSnippets={true}
								value={this.state.docOutput}
								width='100%'
							/>
						</Grid>
				  	</Grid>
				  </Grid>
				 </Grid>
			</Fragment>
		);
	}

}

export default ExcelToProperties;