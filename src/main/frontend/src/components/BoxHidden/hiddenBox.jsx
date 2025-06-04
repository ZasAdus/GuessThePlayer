import './hiddenBox.css'
import '../../styles/boxForHint.css'
function BoxForHintString({ value, category }) {
    if (!value) return null;
    return (
        <>
            <div className="hiddenBox">
                <p className="value">{value}</p>
            </div>
        </>
    );
}
export default BoxForHintString;