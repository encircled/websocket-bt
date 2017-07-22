let stomp = Stomp.over(new SockJS('auction'));

class Header extends React.Component {
    state = {};

    render() {
        return (
            <nav className="navbar navbar-toggleable-md navbar-inverse bg-inverse">
                <button className="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"/>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <ReactRouter.IndexLink activeClassName="active" className="nav-link"
                                                   to="/">Dashboard</ReactRouter.IndexLink>
                        </li>
                        <li className="nav-item">
                            <ReactRouter.Link activeClassName="active" className="nav-link"
                                              to="/about">About</ReactRouter.Link>
                        </li>
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" href="http://example.com"
                               id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true"
                               aria-expanded="false">
                                Dropdown link
                            </a>
                            <div className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                <a className="dropdown-item" href="#">Action</a>
                                <a className="dropdown-item" href="#">Another action</a>
                                <a className="dropdown-item" href="#">Something else here</a>
                            </div>
                        </li>
                    </ul>
                    <div className="form-inline my-2 my-lg-0">
                        <input className="form-control mr-sm-2" type="text" placeholder="Search"/>
                        <ReactRouter.Link className="btn btn-outline-success my-2 my-sm-0"
                                          to="/search/some/val">About</ReactRouter.Link>
                    </div>
                </div>
            </nav>
        )
    }
}

class App extends React.Component {
    render() {
        return (
            <div>
                <Header />
                <div className="content">
                    {this.props.children}
                </div>
            </div>
        )
    }
}

class About extends React.Component {
    render() {
        return (
            <div className="row">About...</div>
        )
    }
}

class Dashboard extends React.Component {
    state = {
        auctions: [],
    };

    componentDidMount() {
        let self = this;
        stomp.subscribe('/app/dashboard', function (message) {
            let response = JSON.parse(message.body);
            console.log(response);
            self.setState({
                auctions: response
            })
        });
    }

    render() {
        let style = {};

        const nodes = this.state.auctions.map(function (item) {
            return (
                <AuctionPreview auction={item}/>
            );
        });
        return (
            <div className="card-deck" style={style}>
                {nodes}
            </div>
        );
    }
}

class Search extends React.Component {
    render() {
        return (
            <div>{'needle ' + this.props.params.needle + ' in a ' + this.props.params.category}</div>
        )
    }
}

const AuctionPreview = React.createClass({
    getInitialState(){
        return {}
    },
    componentDidMount(){

    },
    render: function () {
        let style = {};
        let butNow = this.props.auction.buyNow ?
            <p className="card-text">Buy out price: {this.props.auction.buyNowPrice} Kč</p> : '';

        return (
            <div className="col-sm-4 auction-preview-wrap mb-4">
                <div className="card" style={style}>
                    <div className="card-header text-center">
                        <h4 className="card-title">{this.props.auction.name}</h4>
                    </div>
                    <ImageCarousel auction={this.props.auction}/>
                    <div className="card-block">
                        <p className="card-text">{this.props.auction.name}</p>
                        {butNow}
                        <a href="#" className="btn btn-primary">Shop now</a>
                    </div>
                </div>
            </div>
        );
    }
});

const ImageCarousel = React.createClass({
    getInitialState(){
        return {}
    },
    componentDidMount(){

    },
    render: function () {
        let id = ('preview' + Math.random()).replace(".", "");

        const links = this.props.auction.imageNames.map(function (a, i) {
            return (
                <li data-target={'#' + id} data-slide-to={i} className={i == 0 ? 'active' : ''}/>
            )
        });
        const images = this.props.auction.imageNames.map(function (imageName, i) {
            return (
                <div className={'carousel-item ' + (i == 0 ? 'active' : '')}>
                    <img className="d-block img-fluid" src={'img/' + imageName} alt={imageName}/>
                </div>
            );
        });

        return (
            <div id={id} className="carousel slide auction-preview-wrap_carousel-wrap" data-ride="carousel">
                <ol className="carousel-indicators">
                    {links}
                </ol>
                <div className="carousel-inner" role="listbox">
                    {images}
                </div>
                <a className="carousel-control-prev" href={'#' + id} role="button" data-slide="prev">
                    <span className="carousel-control-prev-icon" aria-hidden="true"/>
                    <span className="sr-only">Previous</span>
                </a>
                <a className="carousel-control-next" href={'#' + id} role="button" data-slide="next">
                    <span className="carousel-control-next-icon" aria-hidden="true"/>
                    <span className="sr-only">Next</span>
                </a>
            </div>
        )
    }
});

/* REDUX */

function search(text) {
    return {
        wsRequestResponse:{
            location: '/app/search',
            payload: {
                needle: text
            },
            messageType: 'SEARCH'
        }
    };
}

function reduxAction(state, action) {
    switch (action.type) {
        case 'SEARCH':
            // handle action and return new state here
            console.log('Redux: search ' + action.content)
    }
}

const websocketCall = store => next => action => {
    if (action.wsRequestResponse) {
        console.log('DO ws call');

        stomp.send(action.wsRequestResponse.location, {}, JSON.stringify(action.wsRequestResponse.payload));

        /*dispatch({
           type:  action.wsMessage.messageType,
            content: ''
        });*/
    } else {
        console.log('skip ws call');
        return next(action);
    }
};

let store = Redux.createStore(reduxAction, Redux.applyMiddleware(websocketCall));

function initConnection() {
    stomp.connect('admin', 'admin', function () {
        window.onunload = function () {
            stomp.disconnect()
        };

        stomp.subscribe('/user/queue/errors', function (message) {
            console.log('ERROR!:' + message)
        });

        ReactDOM.render((
            <ReactRouter.Router history={ReactRouter.hashHistory}>
                <ReactRouter.Route path="/" component={App}>
                    <ReactRouter.IndexRoute component={Dashboard}/>
                    <ReactRouter.Route path="about" component={About}/>
                    <ReactRouter.Route path="search/:category/:needle" component={Search}/>
                </ReactRouter.Route>
            </ReactRouter.Router>
        ), document.getElementById('react-root'))
    }, function() {
        console.log('ON DC')
        setTimeout(initConnection, 2000);
    });
}

$(document).ready(function () {
    initConnection();
});