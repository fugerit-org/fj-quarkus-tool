import React, { Component, Fragment } from 'react';

import {
	BrowserRouter as Router,
	Routes,
	Route,
	Link
} from "react-router-dom";

import Home from './tool/Home';
import Version from './tool/Version';
import PropsToJson from './tool/PropsToJson';
import ExcelToProperties from './tool/ExcelToProperties';
import { Dialog, DialogTitle, Button, Grid, MenuItem, Select } from "@mui/material";

const homepage = '/fj-quarkus-tool/home';

class Tool extends Component {

	constructor(props) {
		super(props);
		this.handleOpenDialog = this.handleOpenDialog.bind(this);
		this.handleCloseDialog = this.handleCloseDialog.bind(this);
		this.state =  {  dialogMessage: null }
	}

	handleCloseDialog = () => {
		this.setState(
			{ 
				dialogMessage: null				 
			}
		);
	};

	handleOpenDialog = ( message ) => {
		this.setState(
			{ 
				dialogMessage: message				 
			}
		);
	};

	render() {
		
		let dialog = <Fragment></Fragment>;
		if ( this.state?.dialogMessage != null) {
			dialog = <Dialog open={true}>
        		<DialogTitle>{this.state.dialogMessage}</DialogTitle>
				<Button  color="primary" onClick={this.handleCloseDialog} autoFocus>Close</Button>        		
        	</Dialog>
		}
		
		return (
			<Router>
				<div className="App">

					<Version/>

					<Grid container spacing={4} columns={{ xs: 16 }}>
					  <Grid item xs={4}><Link to={homepage}><Button color="primary">Home</Button></Link></Grid>
						<Grid item xs={4}><Link to={homepage + "/tool_fun/props_to_json"}><Button color="primary">Properties to JSON</Button></Link></Grid>
						<Grid item xs={4}><Link to={homepage + "/tool_fun/excel_to_props"}><Button color="primary">Excel to Properties</Button></Link></Grid>
					</Grid>

					{dialog}
	
					<Routes>
						<Route path={homepage + "/tool_fun/props_to_json"} element={<PropsToJson handleOpenDialog={this.handleOpenDialog} />} />
						<Route path={homepage + "/tool_fun/excel_to_props"} element={<ExcelToProperties handleOpenDialog={this.handleOpenDialog} />} />
						<Route path="*" element={<Home />} />
					</Routes>

				</div>
			</Router>
		);
	}

}

export default Tool;