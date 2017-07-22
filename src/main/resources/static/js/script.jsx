let stomp = Stomp.over(new SockJS('auction'));

const Header = ({onSearchInput, onReset, onDecrement, onToggleAutoBids, currentCustomer}) => (
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
                                      to="/orders">My orders</ReactRouter.Link>
                </li>
                <li className="nav-item">
                    <a href="logout" className="nav-link">Logout</a>
                </li>
                <li className="nav-item dropdown">
                    <a className="nav-link dropdown-toggle" href="http://example.com"
                       id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true"
                       aria-expanded="false">
                        Admin
                    </a>
                    <div className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <a className="dropdown-item" href="#" onClick={onReset}>Reset all</a>
                        <a className="dropdown-item" onClick={onDecrement}>Decrement countdowns</a>
                        <a className="dropdown-item" onClick={onToggleAutoBids}>Toggle system auto bids</a>
                    </div>
                </li>
            </ul>
            <form className="form-inline my-2 my-lg-0">
                <input className="form-control mr-sm-2" type="text" id="searchInput" placeholder="Search"
                       onInput={onSearchInput}/>
            </form>
        </div>
    </nav>
);

const App = (props) => (
    <div>
        <Header onSearchInput={props.route.onSearchInput} onReset={props.route.onReset}
                onDecrement={props.route.onDecrement} onToggleAutoBids={props.route.onToggleAutoBids}
                currentCustomer={props.route.currentCustomer}/>
        <div className="content">
            {props.children}
        </div>
    </div>
);

const Dashboard = ({auctions}) => (
    <div className="card-deck">
        {(auctions || []).map(item =>
            <AuctionPreview key={item.id} auction={item}/>
        )}
    </div>
);

Dashboard.propTypes = {
    found: React.PropTypes.arrayOf(React.PropTypes.shape({
        id: React.PropTypes.number.isRequired,
        name: React.PropTypes.string.isRequired,
    }).isRequired)
};

const Orders = ({orders}) => (
    <div className="card-deck">
        {(orders || []).map(item =>
            <OrderDetail order={item}/>
        )}
    </div>
);

const OrderDetail = ({order}) => (
    <div className="col-sm-4 auction-preview-wrap mb-4">
        <div className="card">
            <div className="card-header">
                <h4 className="card-title">{order.auctionItem.name}</h4>
                <p>Ordered at {order.orderDateFormatted}<br/>
                    Final price is {order.amount}
                </p>
            </div>
            <ImageCarousel key={order.auctionItem.id} auction={order.auctionItem}/>
            <div className="card-block">
                <p className="card-text">{order.auctionItem.description}</p>
            </div>
        </div>
    </div>



);

const Detail = ({auction, buyFunction, bidFunction}) => (
    auction ?
        <div className="col-sm-12 mt-2">
            <div className="card">
                <div className="card-header ">
                    <h4 className="card-title">{auction.name}</h4>
                </div>
                <div className="card-block">
                    <div className="row">
                        <div className="col-sm-4 auction-preview-wrap mb-4">
                            <ImageCarousel key={auction.id} auction={auction}/>
                        </div>
                        <div className="col-sm-6 auction-preview-wrap mb-4">
                            <div className="col-sm-10">
                                <ReduxDetailBid />
                            </div>
                            <div className="col-sm-10">
                                <div className="input-group">
                                    <span className="input-group-addon">Kč</span>
                                    <input type="text" className="form-control" placeholder="Amount..." id="bidInput"
                                           aria-label="Amount"/>
                                    <span className="input-group-btn">
                                        <button onClick={bidFunction} className="btn btn-primary" type="button">Place bid</button>
                                    </span>
                                </div>
                            </div>
                            <div className="col-sm-10 mt-5">
                                <p>Auction ends at <b>{auction.finishDateFormatted}</b></p>
                            </div>
                        </div>
                    </div>
                    <p className="card-text">{auction.description}</p>
                    <button onClick={buyFunction} type="button" className="btn btn-primary">
                        Buy for {auction.buyNowPrice} Kč
                    </button>
                </div>
            </div>
        </div>

        : <div>Loading...</div>
);

const DetailBid = ({bids, highestBid, currentCustomer}) => (
    highestBid ?
        <div>
            <p>
                {currentCustomer === highestBid.customerName ? 'Your bid is highest: ' : 'Current max bid: '}
                <b>{highestBid.amount} Kč</b> (total bids count is {bids.length})
            </p>
            <ul>Latest bids:
                {bids.slice(bids.length > 2 ? bids.length - 3 : bids.length - 2).reverse().map(item =>
                    <li>
                        <Bid bid={item}/>
                    </li>
                )}
            </ul>
        </div>

        : <div>No bids yet!</div>
);

const Bid = ({bid}) => (
    <span>User <i>{bid.customerName}</i>, <b>{bid.amount} Kč</b> at {dateToString(new Date(bid.bidDate))}</span>
);

const ReduxDetailBid = ReactRedux.connect(
    (state) => {
        let bids = state.reduxAction.bids;
        return {
            currentCustomer: state.reduxAction.currentCustomer,
            bids: bids,
            highestBid: bids && bids.length > 0 ? bids[bids.length - 1] : null
        }
    }
)(DetailBid);

const AuctionPreview = ({auction}) => (
    <div className="col-sm-4 auction-preview-wrap mb-4">
        <div className="card">
            <div className="card-header text-center">
                <h4 className="card-title">{auction.name}</h4>
            </div>
            <ImageCarousel key={auction.id} auction={auction}/>
            <div className="card-block">
                <p className="card-text">{auction.description}</p>
                <p className="card-text">Buy now for {auction.buyNowPrice} Kč</p>
                <ReactRouter.Link className="btn btn-primary"
                                  to={'/auction/' + auction.id}>Shop now</ReactRouter.Link>
            </div>
        </div>
    </div>
);

AuctionPreview.propTypes = {
    auction: React.PropTypes.shape({
        id: React.PropTypes.number.isRequired,
        name: React.PropTypes.string.isRequired,
        description: React.PropTypes.string.isRequired,
        imageNames: React.PropTypes.array.isRequired
    }).isRequired
};

const ImageCarousel = React.createClass({
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

function dateToString(d) {
    return ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2) + " (" + ("0" + d.getDate()).slice(-2) + "-" + ("0" + (d.getMonth() + 1)).slice(-2) + "-" +
        d.getFullYear() + ")";
}

/* REDUX */

function search(text) {
    return {
        wsRequestResponse: {
            location: 'search',
            payload: {
                needle: text
            },
        },
        type: 'SEARCH'
    };
}

function detailBids(bids) {
    return {
        content: bids,
        type: 'DETAIL_BIDS'
    };
}

let detailSubscription;

function detail(id) {
    if (detailSubscription) {
        detailSubscription.unsubscribe();
    }
    detailSubscription = stomp.subscribe('/topic/auction/' + id, function (message) {
        store.dispatch(detailBids(JSON.parse(message.body)));
    });

    return {
        wsRequestResponse: {
            location: 'auction',
            payload: id
        },
        type: 'DETAIL'
    };
}

function loadOrders() {
    return {
        wsRequestResponse: {
            location: 'orders',
            payload: {}
        },
        type: 'LOAD_ORDERS'
    };
}

function placeOrder(id) {
    return {
        wsRequestResponse: {
            location: 'auction/order',
            payload: id
        },
        type: 'PLACE_ORDER'
    };
}

function placeBid(id, amount) {
    return {
        wsRequestResponse: {
            location: 'auction/bid',
            payload: {
                auctionId: id,
                amount: amount
            }
        },
        type: 'PLACE_BID'
    };
}


function loadDashboard() {
    return {
        wsRequestResponse: {
            location: 'dashboard',
            payload: {},
        },
        type: 'DASHBOARD'
    };
}
function setCurrentCustomer(customer) {
    return {
        content: customer,
        type: 'CURRENT_CUSTOMER'
    };
}

const reduxAction = (state = [], action) => {
    switch (action.type) {
        case 'SEARCH':
            return Object.assign({}, state, {
                dashboard: action.content
            });
        case 'DETAIL':
            return Object.assign({}, state, {
                auction: action.content,
                bids: action.content ? action.content.bids : [],
            });
        case 'DETAIL_BIDS':
            return Object.assign({}, state, {
                bids: action.content
            });
        case 'DASHBOARD':
            return Object.assign({}, state, {
                dashboard: action.content
            });
        case 'LOAD_ORDERS':
            return Object.assign({}, state, {
                orders: action.content
            });
        case 'CURRENT_CUSTOMER':
            return Object.assign({}, state, {
                currentCustomer: action.content.name
            });
        default:
            return state
    }
};

const websocketCall = store => next => action => {
    if (action.wsRequestResponse) {
        let wsResource = action.wsRequestResponse.location;
        let subscribe = stomp.subscribe('/user/queue/' + wsResource, function (message) {
            store.dispatch({
                type: action.type,
                content: JSON.parse(message.body)
            });
            subscribe.unsubscribe();
        }, {simpUser: 'admin'});
        stomp.send('/app/' + wsResource, {}, JSON.stringify(action.wsRequestResponse.payload));

    } else {
        return next(action);
    }
};

let store = Redux.createStore(Redux.combineReducers({
    reduxAction,
    routing: ReactRouterRedux.routerReducer
}), Redux.applyMiddleware(websocketCall));

const browserHistory = ReactRouterRedux.syncHistoryWithStore(ReactRouter.hashHistory, store);
browserHistory.listen(location => console.log(location.pathname));

$(document).ready(function () {
    $.get("current", function (data) {
        console.log(data);
        store.dispatch(setCurrentCustomer(data));
        init();
    });
});

function init() {
    stomp.connect('admin', 'admin', function () {
        window.onunload = function () {
            stomp.disconnect()
        };

        let searchTimeout;
        let onSearchInput = function (e) {
            if (searchTimeout) {
                clearTimeout(searchTimeout);
            }
            let value = e.target.value;
            searchTimeout = setTimeout(function () {
                store.dispatch(search(value));
            }, 500)

        };

        let onToggleAutoBids = function () {
            stomp.send('/app/admin/togglebids', {}, {});
        };

        let onReset = function () {
            stomp.send('/app/admin/reset', {}, {});
            setTimeout(function () {
                store.dispatch(loadDashboard());
                store.dispatch(loadOrders());
            }, 800)
        };

        let onDecrement = function () {
            stomp.send('/app/admin/decrement', {}, {});
            setTimeout(function () {
                if (store.getState().reduxAction.auction) {
                    store.dispatch(detail(store.getState().reduxAction.auction.id));
                }
            }, 800);
        };

        const ReduxSearchDash = ReactRedux.connect(
            (state) => {
                return {auctions: state.reduxAction.dashboard}
            }
        )(Dashboard);

        const ReduxDetail = ReactRedux.connect(
            (state) => {
                return {
                    auction: state.reduxAction.auction,
                    buyFunction: function () {
                        if (state.reduxAction.auction) {
                            store.dispatch(placeOrder(state.reduxAction.auction.id));
                            browserHistory.push("orders");
                        }
                    },
                    bidFunction: function () {
                        if (state.reduxAction.auction) {
                            store.dispatch(placeBid(state.reduxAction.auction.id, $('#bidInput').val()));
                        }
                    }
                }
            }
        )(Detail);

        const ReduxOrders = ReactRedux.connect(
            (state) => {
                return {orders: state.reduxAction.orders}
            }
        )(Orders);

        ReactDOM.render((
            <ReactRedux.Provider store={store}>
                <ReactRouter.Router history={browserHistory}>
                    <ReactRouter.Route path="/" component={App} onSearchInput={onSearchInput} onReset={onReset}
                                       onDecrement={onDecrement} onToggleAutoBids={onToggleAutoBids}
                                       currentCustomer={store.getState().reduxAction.currentCustomer}>

                        <ReactRouter.IndexRoute component={ReduxSearchDash}
                                                onEnter={() => store.dispatch(loadDashboard())}/>

                        <ReactRouter.Route path="orders" component={ReduxOrders}
                                           onEnter={() => store.dispatch(loadOrders())}/>

                        <ReactRouter.Route path="auction/:id" component={ReduxDetail}
                                           onEnter={(rout) => store.dispatch(detail(rout.params.id))}/>
                    </ReactRouter.Route>
                </ReactRouter.Router>
            </ReactRedux.Provider>
        ), document.getElementById('react-root'));


        stomp.subscribe('/user/queue/errors', function (message) {
            alert('Unexpected error: ' + message.body)
        });
    }, function (message) {
        alert("WebSocket error: " + message);
    });
}