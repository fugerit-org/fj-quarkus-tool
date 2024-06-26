import React, { Component, Fragment } from 'react';

import { Link } from "react-router-dom";

import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import ModeEditOutlineIcon from '@mui/icons-material/ModeEditOutline';
import ArticleIcon from '@mui/icons-material/Article';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import DoneIcon from '@mui/icons-material/Done';
import {CopyToClipboard} from 'react-copy-to-clipboard';
import Info from './Info';


const homepage = '/fj-quarkus-tool/home';

const cmdDockerRun = 'docker run -it -p 8080:8080 --name fj-quarkus-tool fugeritorg/fj-quarkus-tool:snapshot';

class Home extends Component {

	render() {
		return <Fragment>
			<h1>This project provides tools based on <a href="https://github.com/fugerit-org/">Fugerit Org</a> project.</h1>
			<List sx={{ width: '100%', maxWidth: 800 }}>
				<ListItem key='1'>
					<ListItemAvatar>
						<Avatar>
							<ModeEditOutlineIcon />
						</Avatar>
					</ListItemAvatar>
					<Link to={homepage + "/tool_fun/props_to_json"}>
						<ListItemText primary="Convert property document (java) to JSON."
							secondary='Allow for duplication check' />
					</Link>
				</ListItem>
				<ListItem key='1'>
					<ListItemAvatar>
						<Avatar>
							<ArticleIcon />
						</Avatar>
					</ListItemAvatar>
					<Link to={homepage + "/tool_fun/excel_to_props"}>
						<ListItemText primary="Convert excel to properties."
									  secondary='One column for the key, one column for the value' />
					</Link>
				</ListItem>
			</List>
			
			<Grid container spacing={1} style={{ padding: 10 }}>
			  <Grid item xs={12} align="left">
					<p>To run locally, <a href="https://github.com/fugerit-org/fj-quarkus-tool">go to the project page</a> or use a public image : </p>
			  </Grid>
			  <Grid item xs={11}>
					<TextField style={{ width: '100%' }} id="outlined-basic" label="docker run" variant="outlined" value={cmdDockerRun} />
			  </Grid>
			  <Grid item xs={1} align="left">
					<CopyToClipboard text={cmdDockerRun}><Button><ContentCopyIcon /></Button></CopyToClipboard>
			  </Grid>
			  <Grid item xs={12} align="left">
					And open <a href="http://localhost:8080/fj-quarkus-tool/home/">http://localhost:8080/fj-quarkus-tool/home/</a>
			  </Grid>
			  <Grid item xs={12} align="left">
					Note : See <a href="https://hub.docker.com/repository/docker/fugeritorg/fj-quarkus-tool/general">docker repository</a> for more tags (for instance 'latest' stable or specific version).
			  </Grid>
			  <Grid item xs={12} align="left">
					<Info/>
			  </Grid>
			</Grid>

		</Fragment>
	}

}

export default Home;