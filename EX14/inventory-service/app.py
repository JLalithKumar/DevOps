from flask import Flask
import requests

from opentelemetry import trace

from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter

from opentelemetry.instrumentation.flask import FlaskInstrumentor
from opentelemetry.instrumentation.requests import RequestsInstrumentor


trace.set_tracer_provider(TracerProvider())

span_processor = BatchSpanProcessor(
    OTLPSpanExporter(endpoint="http://tempo:4318/v1/traces")
)

trace.get_tracer_provider().add_span_processor(span_processor)


app = Flask(__name__)

FlaskInstrumentor().instrument_app(app)
RequestsInstrumentor().instrument()


@app.route("/inventory")
def inventory():

    response = requests.get("http://payment-service:5002/payment")

    return response.text


@app.route("/")
def home():

    return "Inventory Service Running"


app.run(host="0.0.0.0", port=5001)