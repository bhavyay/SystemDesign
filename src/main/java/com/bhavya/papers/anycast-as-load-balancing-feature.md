<h2> Anycast as Load Balancing Feature </h2>

[Link to the paper](https://drive.google.com/file/d/1209iTFzMJQDqkPNCneLSp56RyXZqfxoa/view)

<h3>Notes</h3>

<h4>Traditional Load Balancing</h4>

<h4>Basics</h4>
- Anycast is a network routing technique where many machines share the exact same IP address.
- Clients trying to reach that IP address are automatically routed to the "nearest" server.

<h4>How implementation works</h4>
Using Anycast for failover between Load Balancing clusters allows services behind the Load Balancers to benefit from Anycast's capabilities. This approach means that if one Load Balancing cluster becomes unavailable or is underperforming, traffic can automatically reroute to another cluster without manual intervention. This rerouting is based on the principle that Anycast allows multiple devices to share the same IP address, and network traffic is directed to the nearest (in terms of routing distance) available instance. It enhances the reliability and availability of services by ensuring that there is always a backup cluster ready to take over in case the primary one fails, thus minimizing downtime and improving the overall user experience.

Anycast minimizes the infrastructure required to distribute and manage network traffic paths. In a traditional network setup, multiple devices might be responsible for announcing the availability of paths to direct traffic. BGP is preferred for its hierarchical route advertising capabilities, although other protocols can also be effective. Anycast eliminates the need for remote failover solutions involving proxies, leading to a more straightforward, direct connection to failover sites. This direct routing preserves client identity information and avoids the latency and resource overhead associated with proxy-based traffic rerouting.